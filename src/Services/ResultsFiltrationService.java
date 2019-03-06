package Services;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Abstract.Specifications.Specification;
import Abstract.WebPageObject;
import Models.Engines.WebUrlEngine;
import Models.RequestData;
import Models.SearchSettings;
import org.jsoup.nodes.Element;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public void initExceptionsKeywords(File settingsFile) {
        if (settingsFile == null) {
            Logger.error("Cannot initialize input exceptions file");
            throw new NullPointerException();
        }
        Path settingsFilePath = settingsFile.toPath();

        SearchSettings se = new SearchSettings();
        try {
            List<String> lines = Files.readAllLines(settingsFilePath, StandardCharsets.UTF_8);
            lines.removeIf(l -> l.equals(""));

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("# Exceptions for found domains:")) {
                    se.domainExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for words in domain URLs:")) {
                    se.URLExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions meta titles:")) {
                    se.metaTagsExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for top level domains:")) {
                    se.topLevelDomainsExceptions = new ArrayList<>(collectTerms(i, lines));
                }
            }
        } catch (IOException e) {
            Logger.error(e, "Cannot initialize input exceptions file");
        }
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
