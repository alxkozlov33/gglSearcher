package Abstract;

import com.opencsv.bean.CsvBindByName;

public abstract class OutputCSVModelGeoData {

    @CsvBindByName(column = "Country")
    public String country;

    @CsvBindByName(column = "City")
    public String city;

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
}
