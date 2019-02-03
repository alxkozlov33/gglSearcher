package Models;

import com.opencsv.bean.CsvBindByName;

public class InputCsvModelItem {

    public InputCsvModelItem() {
    }

    @CsvBindByName(column = "cities")
    private String city;

    public String getCity() {
        return city;
    }
}