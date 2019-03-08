package Services;

import Models.InputCsvModelItem;
import Utils.DirUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.collections4.IteratorUtils;
import org.tinylog.Logger;
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

    public void initInputDataFile(File filePath) {
        if (DirUtils.isFileOk(filePath, "csv")) {
            inputDataFile = filePath;
        }
    }

    public void initCSVItems(File inputFilePath) {
        initInputDataFile(inputFilePath);
        if(!DirUtils.isFileOk(inputFilePath, "csv")) {
            return;
        }
        try {
            Reader reader = Files.newBufferedReader(Paths.get(getInputDataFile().getAbsolutePath()));
            CsvToBean<InputCsvModelItem> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(InputCsvModelItem.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            inputCsvModelItems = new ArrayList(IteratorUtils.toList(csvToBean.iterator()));
            reader.close();
        } catch (Exception ex) {
            Logger.tag("SYSTEM").error(ex, "Something wrong with input file");
        }
    }

    public List<InputCsvModelItem> getInputFileData() {
        return inputCsvModelItems;
    }

    public void clearInputDataFile() {
        inputDataFile = null;
    }
}
