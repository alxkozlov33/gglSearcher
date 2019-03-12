package Abstract.Models.SearchResultModels;

public class ListSearchResultItem extends GoogleSearchResultItem {

    public String city;
    public ListSearchResultItem(String mainHeader, String link, String description) {
        super(mainHeader, link, description);
    }

    public ListSearchResultItem(String mainHeader, String link, String description, String city) {
        super(mainHeader, link, description);
        this.city = city;
    }
}
