package Models;

import com.opencsv.bean.CsvBindByPosition;

public class InputCsvModelItem {

    public InputCsvModelItem() {
    }

    @CsvBindByPosition(position = 0)
    private String city;


    public String getCity() {
        return city;
    }
}