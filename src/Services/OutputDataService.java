package Services;

import Abstract.Models.OutputModels.IOutputModel;
import Utils.CSVUtils;
import Utils.DirUtils;
import com.opencsv.CSVWriter;
import org.tinylog.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputDataService {

    private static File outputFolder;
    private static File outputFile;

    public OutputDataService() {
    }

    private void createEmptyCSVFile(File outputFile) {
        FileWriter mFileWriter;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile());
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
            mCsvWriter.writeNext(new String[]{"Gallery name", "Website", "Not sure", "Html page title", "City", "Country"});
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot create empty output file");
        }
    }

    public void createOutputFile(String placeHolder) {
        String fileNameFromPlaceHolder = placeHolder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
        outputFile = new File(outputFolder + File.separator + fileNameFromPlaceHolder + ".csv");
        createEmptyCSVFile(outputFile);
    }

    public File getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFile(File outputFolder) {
        if (DirUtils.isDirOk(outputFolder)) {
            this.outputFolder = outputFolder;

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

    public void clearOutputFile() {
        outputFolder = null;
        outputFile = null;
    }
}
