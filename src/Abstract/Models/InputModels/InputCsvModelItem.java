package Abstract.Models.InputModels;

import com.opencsv.bean.CsvBindByPosition;

public class InputCsvModelItem {

    public InputCsvModelItem() {
    }

    @CsvBindByPosition(position = 0)
    private String columnA;

    @CsvBindByPosition(position = 1)
    private String columnB;

    @CsvBindByPosition(position = 2)
    private String columnC;

    @CsvBindByPosition(position = 3)
    private String columnD;

    @CsvBindByPosition(position = 4)
    private String columnE;

    @CsvBindByPosition(position = 5)
    private String columnF;

    @CsvBindByPosition(position = 6)
    private String columnG;

    @CsvBindByPosition(position = 7)
    private String columnH;

    @CsvBindByPosition(position = 8)
    private String columnI;

    @CsvBindByPosition(position = 9)
    private String columnJ;

    @CsvBindByPosition(position = 10)
    private String columnK;

    @CsvBindByPosition(position = 11)
    private String columnL;

    @CsvBindByPosition(position = 12)
    private String columnM;

    @CsvBindByPosition(position = 13)
    private String columnN;

    @CsvBindByPosition(position = 14)
    private String columnO;

    public String getColumnA() {
        return columnA;
    }

    public String getColumnB() {
        return columnB;
    }

    public String getColumnC() {
        return columnC;
    }

    public String getColumnD() {
        return columnD;
    }

    public String getColumnE() {
        return columnE;
    }

    public String getColumnF() {
        return columnF;
    }

    public String getColumnG() {
        return columnG;
    }

    public String getColumnH() {
        return columnH;
    }

    public String getColumnI() {
        return columnI;
    }

    public String getColumnJ() {
        return columnJ;
    }

    public String getColumnK() {
        return columnK;
    }

    public String getColumnL() {
        return columnL;
    }

    public String getColumnM() {
        return columnM;
    }

    public String getColumnN() {
        return columnN;
    }

    public String getColumnO() {
        return columnO;
    }
}