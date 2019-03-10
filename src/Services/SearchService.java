package Services;

import Abstract.Engines.WebUrlEngine;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.SearchResultModels.WebPageObject;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Models.RequestData;
import Utils.ResultsUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public WebPageObject getWebSitePageSource(GoogleSearchResultItem item) {
        ProxyService proxyService = new ProxyService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();
        RequestData requestData = new RequestData(item.getLink(), userAgentsRotatorService.getRandomUserAgent(), proxyService.getNewProxyAddress());
        Element element = new WebUrlEngine().getWebSourceData(requestData);
        return parseSourceData(element);
    }

    private WebPageObject parseSourceData(Element html){
        if (html == null) {
            return null;
        }
        String siteDescription = html.select("meta[name=description]").attr("content");
        String siteKeywords = html.select("meta[name=keywords]").attr("content");
        String siteName = html.select("meta[property=og:title]").attr("content");
        if (StringUtils.isEmpty(siteName)) {
            siteName = html.select("title").text();
        }
        return new WebPageObject(siteDescription, siteKeywords, siteName);
    }

    public <T extends GoogleSearchResultItem> ArrayList filterGoogleResultData(List<T> googleSearchResults) {
        SettingsService settingsService = new SettingsService();

        AbstractSpecification<GoogleSearchResultItem> googleItemsSpec =
                new DomainExceptionsSpecification(settingsService.getSearchSettings().domainExceptions)
                        .and(new TopLevelDomainExceptionsSpecification(settingsService.getSearchSettings().topLevelDomainsExceptions))
                        .and(new URLExceptionsSpecification(settingsService.getSearchSettings().URLExceptions));

        return ResultsUtils.filterResults(googleSearchResults, googleItemsSpec);
    }
}
