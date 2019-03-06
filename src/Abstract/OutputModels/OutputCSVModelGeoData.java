package Abstract.OutputModels;

import com.opencsv.bean.CsvBindByName;

public class OutputCSVModelGeoData extends OutputCsvModelItem {
    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "Country")
    private String country;

    public OutputCSVModelGeoData(String City, String Country) {
        super(City, Country);
        this.city = City;
        this.country = Country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
