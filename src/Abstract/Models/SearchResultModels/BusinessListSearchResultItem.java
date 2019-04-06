package Abstract.Models.SearchResultModels;

import Abstract.Models.InputModels.InputCsvModelItem;

public class BusinessListSearchResultItem extends GoogleSearchResultItem {

    private String city;
    private String country;

    public BusinessListSearchResultItem(String mainHeader, String link, String description, String city, String country) {
        super(mainHeader, link, description);
        this.city = city;
        this.country = country;
    }

    public BusinessListSearchResultItem(String mainHeader, String link, String description, InputCsvModelItem inputCsvModelItem) {
        super(mainHeader, link, description);
        if (inputCsvModelItem != null) {
            this.city = inputCsvModelItem.getColumnA();
            this.country = inputCsvModelItem.getColumnC();
        }
    }

    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
}
