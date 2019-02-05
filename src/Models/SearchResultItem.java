package Models;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.io.IOException;

public class SearchResultItem {
    private String MainHeader;
    private String SearchedLink;
    private String Description;
    private boolean isItemCorrect;

    private String Website;
    private String GalleryName;
    private String Address;

    public SearchResultItem(Element div) {
        MainHeader = div.select("a").first().select("h3").text();
        SearchedLink = div.select("a").first().attr("href");
        Description = div.select("div.s").text();
        getItemSource();
    }
    public SearchResultItem(String GalleryName, String Address, String Website) {
        this.GalleryName = GalleryName;
        this.Address = Address;
        this.Website = Website;
        isItemCorrect = true;
    }

    public void getItemSource() {
        try {
            if (!StringUtils.isEmpty(Description)) {
                Connection.Response response = Jsoup.connect(SearchedLink)
                        .followRedirects(true)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                        .method(Connection.Method.GET)
                        .execute();
                isItemCorrect = checkIfSourceContainTags(response.parse());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfSourceContainTags(Element source) {
        String siteDescription = source.select("meta[name=description]").attr("content");
        String siteKeywords = source.select("meta[name=keywords]").attr("content");
        String siteName = source.select("meta[property=og:title]").attr("content");

        if (siteDescription.contains("gallery") || (siteDescription.contains("art,") || siteDescription.contains("art ")) ||
                siteKeywords.contains("gallery") || (siteDescription.contains("art,") || siteDescription.contains("art ")) ||
                siteName.contains("gallery") || (siteDescription.contains("art,") || siteDescription.contains("art "))) {
            Website = SearchedLink;
            GalleryName = siteName;
            return true;
        }
        return false;
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

    public void setItemCorrect(boolean itemCorrect) {
        isItemCorrect = itemCorrect;
    }

    public String getWebsite() {
        return Website;
    }

    public String getGalleryName() {
        return GalleryName;
    }

    public String getAddress() {
        return Address;
    }
}