package Services;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Abstract.Specifications.Specification;
import Abstract.WebPageObject;
import Abstract.Engines.WebUrlEngine;
import Models.RequestData;
import Models.SearchSettings;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ResultsFiltrationService {

    private SearchSettings settings;
    WebUrlEngine webUrlEngine = new WebUrlEngine();

    private final ProxyService proxyService;

    public ResultsFiltrationService(ProxyService proxyService) {
        this.proxyService = proxyService;
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

    public WebPageObject getWebPageObject(RequestData requestData) throws IOException {
        Element element = webUrlEngine.getWebSourceData(requestData);
        String siteDescription = element.select("meta[name=description]").attr("content");
        String siteKeywords = element.select("meta[name=keywords]").attr("content");
        String siteName = element.select("meta[property=og:title]").attr("content");

        return new WebPageObject(siteDescription, siteKeywords, siteName);
    }

    public ArrayList filterResultsData(Set<GoogleSearchResultItem> googleSearchResults) {
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(settings.metaTagsExceptions);

        Specification<GoogleSearchResultItem> googleItemsSpec =
                     new DomainExceptionsSpecification(settings.domainExceptions)
                .and(new TopLevelDomainExceptionsSpecification(settings.topLevelDomainsExceptions))
                .and(new URLExceptionsSpecification(settings.URLExceptions));

        return filterResults(googleSearchResults, googleItemsSpec);

//        if (str.length() > 15){
//            NotSureLink = link;
//        }
//        else {
//            Website = StrUtils.extractDomainName(link);
//        }
//
//        if (StringUtils.isEmpty(siteName)) {
//            GalleryName = mainHeader;
//        }
//        else {
//            GalleryName = siteName;
//        }
    }

    private ArrayList<String> collectTerms(int index, List<String> lines) {
        ArrayList<String> buffer = new ArrayList<>();
        for (int k = (index+1); k < lines.size(); k++)
        {
            if (lines.get(k).startsWith("#")) {
                break;
            }
            buffer.add(lines.get(k).trim().toLowerCase());
        }
        return buffer;
    }
}
