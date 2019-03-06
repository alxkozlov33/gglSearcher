package Models.SearchResultsModels;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Models.SearchSettings;
import Services.LogService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class SearchResultItem extends GoogleSearchResultItem {

    private boolean isItemCorrect;

    private static LogService logService;

    private String Website;
    private String GalleryName;
    private String City;
    private String Country;
    private String NotSureLink;

    private SearchSettings se;

    public SearchResultItem() {
    }

    public SearchResultItem parseInputDiv(Element div) {
        mainHeader = div.select("h3").text();
        link = div.select("div.r > a").attr("href");
        if (StringUtils.isEmpty(link)){
            link = div.select("h3.r > a").attr("href");
        }
        description = div.select("div.s").text();
        return this;
    }

    public SearchResultItem initSearchExceptions(SearchSettings se) {
        this.se = se;
        return this;
    }

    public SearchResultItem getItemSource() {
        try {
            if (!StringUtils.isEmpty(description) && !StringUtils.isEmpty(link)) {
                link = StrUtils.clearLink(link);
                Connection.Response response = Jsoup.connect(link)
                        .followRedirects(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .method(Connection.Method.GET)
                        .execute();

                isItemCorrect = checkIfSourceRight(response.parse());
            }
        } catch (Exception e) {
            logService.LogMessage("Link broken: " + link);
            logService.LogMessage(e.getMessage());
        }
        return this;
    }

    private boolean checkIfSourceRight(Element source) {
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

    public String getMainHeader() {
        return mainHeader;
    }

    public String getSearchedLink() {
        return mainHeader;
    }

    public String getDescription() {
        return mainHeader;
    }

    public boolean isItemCorrect() {
        return isItemCorrect;
    }

    public String getWebsite() {
        return Website;
    }

    public String getGalleryName() {
        return GalleryName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getNotSureLink() {
        return NotSureLink;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}