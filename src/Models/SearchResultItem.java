package Models;

import Services.LogService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SearchResultItem {
    private String MainHeader;
    private String SearchedLink;
    private String Description;
    private boolean isItemCorrect;

    private static LogService logService;

    private String Website;
    private String GalleryName;
    private String City;
    private String Country;
    private String NotSureLink;

    private SearchExceptions se;

    public SearchResultItem(LogService logService) {
        this.logService = logService;
    }

    public SearchResultItem parseInputDiv(Element div) {
        MainHeader = div.select("h2.result__title").text();
        SearchedLink = div.select("a.result__url").first().attr("href");
        Description = div.select("a.result__snippet").text();
        return this;
    }

    public SearchResultItem initSearchExceptions(SearchExceptions se) {
        this.se = se;
        return this;
    }

    public SearchResultItem getItemSource() {
        try {
            if (!StringUtils.isEmpty(Description)) {
                Connection.Response response = Jsoup.connect(SearchedLink)
                        .followRedirects(true)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                        .method(Connection.Method.GET)
                        .execute();
                isItemCorrect = checkIfSourceRight(response.parse());
            }
        } catch (IOException e) {
            logService.LogMessage("Link broken: "+ SearchedLink);
        }
        return this;
    }

    private boolean checkIfSourceRight(Element source) {
        String siteDescription = source.select("meta[name=description]").attr("content");
        String siteKeywords = source.select("meta[name=keywords]").attr("content");
        String siteName = source.select("meta[property=og:title]").attr("content");

        for (String metaExceptionKeyword : se.metaTagsExceptions) {
            if (siteDescription.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
            if (siteKeywords.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
        }
        String domainName = StrUtils.extractDomainName(SearchedLink);
        for (String domainNameException: se.domainExceptions) {
            if (domainNameException.toLowerCase().contains(domainName.toLowerCase())) {
                return false;
            }
        }

        String str = StrUtils.getUnmatchedPartOfString(SearchedLink);
        for (String urlException: se.URLExceptions) {
            if (str.toLowerCase().contains(urlException.toLowerCase())) {
                return false;
            }
        }

        for (String topLevelDomainException: se.topLevelDomainsExceptions) {
            if (SearchedLink.toLowerCase().contains(topLevelDomainException.toLowerCase())) {
                return false;
            }
        }

        if (str.length() > 4){
            NotSureLink = SearchedLink;
        }
        else {
            Website = StrUtils.extractDomainName(SearchedLink);
        }

        GalleryName = siteName;
        return true;
    }

    public String getMainHeader() {
        return MainHeader;
    }

    public String getSearchedLink() {
        return SearchedLink;
    }

    public String getDescription() {
        return Description;
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