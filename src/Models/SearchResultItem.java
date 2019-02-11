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
        SearchedLink = StrUtils.normalizeLink(div.selectFirst("a.result__url").text());
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
                String link = StrUtils.normalizeLink(SearchedLink);
                Connection.Response response = Jsoup.connect(link)
                        .followRedirects(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
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