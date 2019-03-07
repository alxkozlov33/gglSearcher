package Services;

import Abstract.Engines.WebUrlEngine;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Abstract.Specifications.Specification;
import Models.RequestData;
import Models.SearchSettings;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Set;

public class SearchService {

    private final ProxyService proxyService;
    private final PropertiesService propertiesService;
    private SearchSettings searchSettings;

    public SearchService() {
        proxyService = new ProxyService();
        propertiesService = new PropertiesService();
        searchSettings = new SearchSettings();
    }

    public void getWebSitePageSource(GoogleSearchResultItem item) {
        ProxyService proxyService = new ProxyService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();

        RequestData requestData = new RequestData(item.link, userAgentsRotatorService.getRandomUserAgent(), proxyService.getNewProxyAddress());

        Element element = new WebUrlEngine().getWebSourceData(requestData);
        WebPageObject webPageObject = parseSourceData(element);
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(searchSettings.metaTagsExceptions);
        metaTagsExceptionsSpecification.isSatisfiedBy(webPageObject);
    }

    private WebPageObject parseSourceData(Element html){
        String siteDescription = html.select("meta[name=description]").attr("content");
        String siteKeywords = html.select("meta[name=keywords]").attr("content");
        String siteName = html.select("meta[property=og:title]").attr("content");
        if (StringUtils.isEmpty(siteName)) {
            siteName = html.select("title").text();
        }
        WebPageObject webPageObject = new WebPageObject(siteDescription, siteKeywords, siteName);
        return webPageObject;
    }

    public ArrayList filterGoogleResultData(Set<GoogleSearchResultItem> googleSearchResults) {
        Specification<GoogleSearchResultItem> googleItemsSpec =
                new DomainExceptionsSpecification(searchSettings.domainExceptions)
                        .and(new TopLevelDomainExceptionsSpecification(searchSettings.topLevelDomainsExceptions))
                        .and(new URLExceptionsSpecification(searchSettings.URLExceptions));

        return filterResults(googleSearchResults, googleItemsSpec);
    }

    <T> ArrayList<T> filterResults(Set<T> set, Specification<T> spec) {
        ArrayList<T> results = new ArrayList<>();
        for(T t : set) {
            if( spec.isSatisfiedBy(t) ) {
                results.add(t);
            }
        }
        return results;
    }
}
