package Services;

import Abstract.Models.OutputModels.IOutputModel;
import Utils.CSVUtils;
import com.opencsv.CSVWriter;
import org.tinylog.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputDataService {

    private File outputFile;

    public OutputDataService() {
    }

    private void createEmptyCSVFile(File outputFile, String[] columns) {
        FileWriter mFileWriter;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile());
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
            mCsvWriter.writeNext(columns);
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot create empty output multiple searching results file");
        }
    }

    public void createOutputFileForMultipleSearchOutput(String placeHolder) {
        String fileNameFromPlaceHolder = placeHolder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
        outputFile = new File(fileNameFromPlaceHolder + ".csv");
        createEmptyCSVFile(outputFile, new String[]{"Gallery name", "Website", "Not sure", "Html page title", "City", "Country"});
    }

    public void createOutputFileForSingleSearchOutput(String placeHolder) {
        String fileNameFromPlaceHolder = placeHolder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
        outputFile = new File(fileNameFromPlaceHolder + ".csv");
        createEmptyCSVFile(outputFile, new String[]{"Main header", "Website", "Not sure", "Html page title"});
    }

    public void saveResultCsvItemsByMultipleSearch(List<IOutputModel> csvFileData) {
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(outputFile.getAbsoluteFile(), true);
            for (IOutputModel item : csvFileData) {
                CSVUtils.writeLine(writer, item.toCsvRowString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot save data to output file");
        }
    }

    public void saveResultCsvItems(List<IOutputModel> csvFileData) {
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(outputFile.getAbsoluteFile(), true);
            for (IOutputModel item : csvFileData) {
                CSVUtils.writeLine(writer, item.toCsvRowString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot save data to output file");
        }
    }
}
