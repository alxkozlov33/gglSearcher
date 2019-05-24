package Services;

import Abstract.Models.InputModels.InputCsvModelItem;
import Utils.DirUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.collections4.IteratorUtils;
import org.tinylog.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    private File getInputDataFile() {
        return inputDataFile;
    }

    public List<InputCsvModelItem> getInputCsvModelItems() {
        return inputCsvModelItems;
    }

    public void initInputFile(File filePath) {
        if (DirUtils.isFileOk(filePath, "csv")) {
            inputDataFile = filePath;
        }
    }

    public void initInputFileData() {
        initInputFile(inputDataFile);
        if(!DirUtils.isFileOk(inputDataFile, "csv")) {
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getInputDataFile().getAbsolutePath()), StandardCharsets.UTF_8));
            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(InputCsvModelItem.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            inputCsvModelItems = new ArrayList(IteratorUtils.toList(csvToBean.iterator()));
            br.close();
        } catch (Exception ex) {
            Logger.tag("SYSTEM").error(ex, "Something wrong with input file");
        }
    }

    public void clearInputDataFile() {
        inputDataFile = null;
    }
}
