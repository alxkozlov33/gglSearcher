package Models;

import org.jsoup.nodes.Element;

public class SearchResultItem {
    private String MainHeader;
    private String SearchedLink;
    private String Description;

    public SearchResultItem(Element div) {
        MainHeader = div.select("a").first().select("h3").text();
        SearchedLink = div.select("a").first().attr("href");
        Description = div.select("div.s").text();
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
}