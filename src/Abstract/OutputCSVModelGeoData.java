package Abstract;

import com.opencsv.bean.CsvBindByName;

public abstract class OutputCSVModelGeoData {

    @CsvBindByName(column = "Country")
    private String country;

    @CsvBindByName(column = "City")
    private String city;

    public OutputCSVModelGeoData(String City, String Country) {
        this.city = City;
        this.country = Country;
    }

    public String getCity() {
        if (city == null) {
            return "";
        }
        return this.city;
    }
    public String getCountry() {
        if (country == null) {
            return "";
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
