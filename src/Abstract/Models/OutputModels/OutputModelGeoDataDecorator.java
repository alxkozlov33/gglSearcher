package Abstract.Models.OutputModels;

import com.opencsv.bean.CsvBindByName;

public class OutputModelGeoDataDecorator implements IOutputModel {

    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "Country")
    private String country;

    IOutputModel decorated;

    public OutputModelGeoDataDecorator(IOutputModel outputRegularCSVItem, String city, String country) {
        this.decorated = outputRegularCSVItem;
        this.city = city;
        this.country = country;
    }

    public String toCsvRowString() {
        return String.format("%s,\"%s\",\"%s\"", decorated.toCsvRowString(), city, country);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
