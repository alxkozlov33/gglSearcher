package Services;

import Models.InputCsvModelItem;
import Utils.DirUtils;
import Utils.StrUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.tinylog.Logger;

import java.awt.*;
import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InputDataService {
    private static List<InputCsvModelItem> inputCsvModelItems;
    private static File inputDataFile;

    public InputDataService() {
        if (inputCsvModelItems == null) {
            inputCsvModelItems = new ArrayList<>();
        }
    }

    public static List<InputCsvModelItem> getInputCsvModelItemsStatic() {
        return inputCsvModelItems;
    }

    public File getInputDataFile() {
        return inputDataFile;
    }

    public List<InputCsvModelItem> getInputCsvModelItems() {
        return inputCsvModelItems;
    }

    public void initInputDataFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            Logger.tag("SYSTEM").info("Input data file path empty");
            return;
        }
        inputDataFile = new File(filePath);
    }

    public ArrayList<InputCsvModelItem> initCSVItems(String inputFilePath, Frame frame) {
        initInputDataFile(inputFilePath);

        ArrayList csvFileData = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(getInputDataFile().getAbsolutePath()));
            CsvToBean<InputCsvModelItem> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(InputCsvModelItem.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            csvFileData = new ArrayList(IteratorUtils.toList(csvToBean.iterator()));
            reader.close();
        } catch (Exception ex) {
            Logger.tag("SYSTEM").error(ex, "Something wrong with input file");
        }
        return csvFileData;
    }

    public void clearInputDataFile() {
        inputDataFile = null;
    }
}
