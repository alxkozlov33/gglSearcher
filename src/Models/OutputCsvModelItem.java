package Models;

import com.opencsv.bean.CsvBindByPosition;

public class OutputCsvModelItem {

    @CsvBindByPosition(position = 0)
    private String companyName;


    public String getCompanyName() {
        return companyName;
    }
}
