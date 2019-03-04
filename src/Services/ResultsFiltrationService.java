package Services;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Models.Engines.WebUrlEngine;
import Models.SearchSettings;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ResultsFiltrationService {
    private SearchSettings settings;

    WebUrlEngine webUrlEngine = new WebUrlEngine();

    private boolean containsMetaTags() {
        for (String metaExceptionKeyword : settings.metaTagsExceptions) {
            if (siteDescription.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
            if (siteKeywords.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfSourceRight(Element source) {
        String siteDescription = source.select("meta[name=description]").attr("content");
        String siteKeywords = source.select("meta[name=keywords]").attr("content");
        String siteName = source.select("meta[property=og:title]").attr("content");
        if (StringUtils.isEmpty(siteName)) {
            siteName = source.select("title").text();
        }

        for (String metaExceptionKeyword : settings.metaTagsExceptions) {
            if (siteDescription.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
            if (siteKeywords.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
        }
        String domainName = StrUtils.extractDomainName(link);
        for (String domainNameException: settings.domainExceptions) {
            if (domainName.toLowerCase().contains(domainNameException.toLowerCase())) {
                return false;
            }
        }

        String str = StrUtils.getUnmatchedPartOfString(link);
        for (String urlException: settings.URLExceptions) {
            if (str.toLowerCase().contains(urlException.toLowerCase())) {
                return false;
            }
        }

        for (String topLevelDomainException: settings.topLevelDomainsExceptions) {
            if (link.toLowerCase().contains(topLevelDomainException.toLowerCase())) {
                return false;
            }
        }

        if (str.length() > 15){
            NotSureLink = link;
        }
        else {
            Website = StrUtils.extractDomainName(link);
        }

        if (StringUtils.isEmpty(siteName)) {
            GalleryName = mainHeader;
        }
        else {
            GalleryName = siteName;
        }
        return true;
    }

    public ArrayList filterData(ArrayList<GoogleSearchResultItem> data) {
        String siteDescription = source.select("meta[name=description]").attr("content");
        String siteKeywords = source.select("meta[name=keywords]").attr("content");
        String siteName = source.select("meta[property=og:title]").attr("content");
        if (StringUtils.isEmpty(siteName)) {
            siteName = source.select("title").text();
        }

        for (String metaExceptionKeyword : se.metaTagsExceptions) {
            if (siteDescription.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
            if (siteKeywords.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
        }
        String domainName = StrUtils.extractDomainName(link);
        for (String domainNameException: se.domainExceptions) {
            if (domainName.toLowerCase().contains(domainNameException.toLowerCase())) {
                return false;
            }
        }

        String str = StrUtils.getUnmatchedPartOfString(link);
        for (String urlException: se.URLExceptions) {
            if (str.toLowerCase().contains(urlException.toLowerCase())) {
                return false;
            }
        }

        for (String topLevelDomainException: se.topLevelDomainsExceptions) {
            if (link.toLowerCase().contains(topLevelDomainException.toLowerCase())) {
                return false;
            }
        }

        if (str.length() > 15){
            NotSureLink = link;
        }
        else {
            Website = StrUtils.extractDomainName(link);
        }

        if (StringUtils.isEmpty(siteName)) {
            GalleryName = mainHeader;
        }
        else {
            GalleryName = siteName;
        }

        return true;
    }

    public void initExceptionsKeywords(File settingsFile) {
        if (settingsFile == null) {
            throw new NullPointerException();
            //logService.LogMessage("Exceptions file path empty");
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
            //logService.LogMessage("Cannot initialize input exceptions file");
            //logService.LogMessage(e.getMessage());
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
