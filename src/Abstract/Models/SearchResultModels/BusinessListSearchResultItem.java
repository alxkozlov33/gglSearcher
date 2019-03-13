package Abstract.Models.SearchResultModels;

public class BusinessListSearchResultItem extends GoogleSearchResultItem {

    private String city;
    private String country;
    public BusinessListSearchResultItem(String mainHeader, String link, String description) {
        super(mainHeader, link, description);
    }

    public BusinessListSearchResultItem(String mainHeader, String link, String description, String city) {
        super(mainHeader, link, description);
        this.city = city;
    }

    public BusinessListSearchResultItem(String mainHeader, String link, String description, String city, String country) {
        super(mainHeader, link, description);
        this.city = city;
        this.country = country;
    }


    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
}
