package Models;

import com.opencsv.bean.CsvBindByName;

public class InputCsvModelItem {

    public InputCsvModelItem() {
    }

    @CsvBindByName(column = "cities")
    private String city;

    @CsvBindByName(column = "name")
    private String name;

    public String getCity() {
        return city;
    }
    public String getName() {
        return name;
    }

    public String getFirstName() {
        return  name.split(" ")[0];
    }

    public String getSecondName() {
        return name.split(" ")[1];
    }
}