package Models.SearchResultsModels;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Models.SearchSettings;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class SearchResultItem {

//    private boolean isItemCorrect;
//
//    private static LogService logService;
//
//    private String Website;
//    private String GalleryName;
//    private String City;
//    private String Country;
//    private String NotSureLink;
//
//    private SearchSettings se;
//
//    public SearchResultItem() {
//    }
//
//    public SearchResultItem parseInputDiv(Element div) {
//        mainHeader = div.select("h3").text();
//        link = div.select("div.r > a").attr("href");
//        if (StringUtils.isEmpty(getLink())){
//            link = div.select("h3.r > a").attr("href");
//        }
//        description = div.select("div.s").text();
//        return this;
//    }
//
//    public SearchResultItem initSearchExceptions(SearchSettings se) {
//        this.se = se;
//        return this;
//    }
//
//    public SearchResultItem getItemSource() {
//        try {
//            if (!StringUtils.isEmpty(super.getDescription()) && !StringUtils.isEmpty(getLink())) {
//                link = StrUtils.clearLink(getLink());
//                Connection.Response response = Jsoup.connect(getLink())
//                        .followRedirects(true)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
//                        .ignoreHttpErrors(true)
//                        .method(Connection.Method.GET)
//                        .execute();
//
//                isItemCorrect = checkIfSourceRight(response.parse());
//            }
//        } catch (Exception e) {
//            logService.LogMessage("Link broken: " + getLink());
//            logService.LogMessage(e.getMessage());
//        }
//        return this;
//    }
//
//    private boolean checkIfSourceRight(Element source) {
//        String siteDescription = source.select("meta[name=description]").attr("content");
//        String siteKeywords = source.select("meta[name=keywords]").attr("content");
//        String siteName = source.select("meta[property=og:title]").attr("content");
//        if (StringUtils.isEmpty(siteName)) {
//            siteName = source.select("title").text();
//        }
//
//        for (String metaExceptionKeyword : se.metaTagsExceptions) {
//            if (siteDescription.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
//                return false;
//            }
//            if (siteKeywords.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
//                return false;
//            }
//        }
//        String domainName = StrUtils.extractDomainName(getLink());
//        for (String domainNameException: se.domainExceptions) {
//            if (domainName.toLowerCase().contains(domainNameException.toLowerCase())) {
//                return false;
//            }
//        }
//
//        String str = StrUtils.getUnmatchedPartOfString(getLink());
//        for (String urlException: se.URLExceptions) {
//            if (str.toLowerCase().contains(urlException.toLowerCase())) {
//                return false;
//            }
//        }
//
//        for (String topLevelDomainException: se.topLevelDomainsExceptions) {
//            if (getLink().toLowerCase().contains(topLevelDomainException.toLowerCase())) {
//                return false;
//            }
//        }
//
//        if (str.length() > 15){
//            NotSureLink = getLink();
//        }
//        else {
//            Website = StrUtils.extractDomainName(getLink());
//        }
//
//        if (StringUtils.isEmpty(siteName)) {
//            GalleryName = super.getMainHeader();
//        }
//        else {
//            GalleryName = siteName;
//        }
//
//        return true;
//    }
//
//    public String getMainHeader() {
//        return super.getMainHeader();
//    }
//
//    public String getSearchedLink() {
//        return super.getMainHeader();
//    }
//
//    public String getDescription() {
//        return super.getMainHeader();
//    }
//
//    public boolean isItemCorrect() {
//        return isItemCorrect;
//    }
//
//    public String getWebsite() {
//        return Website;
//    }
//
//    public String getGalleryName() {
//        return GalleryName;
//    }
//
//    public String getCity() {
//        return City;
//    }
//
//    public void setCity(String city) {
//        City = city;
//    }
//
//    public String getNotSureLink() {
//        return NotSureLink;
//    }
//
//    public String getCountry() {
//        return Country;
//    }
//
//    public void setCountry(String country) {
//        Country = country;
//    }
}