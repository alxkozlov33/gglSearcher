package Abstract.SearchResultModels;

public class GoogleSearchResultItem {

    public GoogleSearchResultItem(String mainHeader, String link, String description/*, String htmlPageTitle*/) {
        this.mainHeader = mainHeader;
        this.link = link;
        this.description = description;
        //this.htmlPageTitle = htmlPageTitle;
    }

    private String mainHeader;
    private String link;
    private String description;
    //private String htmlPageTitle;

    public String getMainHeader() {
        return mainHeader;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    //public String getHtmlPageTitle() { return htmlPageTitle; }
}
