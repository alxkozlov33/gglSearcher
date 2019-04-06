package Abstract.Models.SearchResultModels;

public class GoogleSearchResultItem {

    public GoogleSearchResultItem(String mainHeader, String link, String description) {
        this.mainHeader = mainHeader;
        this.link = link;
        this.description = description;
    }

    private String mainHeader;
    private String link;
    private String description;

    public String getMainHeader() {
        return mainHeader;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public void setLink(String link) { this.link = link; }
}
