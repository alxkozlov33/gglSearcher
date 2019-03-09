package Services;

import Abstract.OutputModels.OutputCsvModelItem;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Utils.DirUtils;
import com.opencsv.CSVWriter;
import org.tinylog.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputDataService {

    private static File outputFolder;
    private static File outputFile;

    public OutputDataService() {
    }

    private void createEmptyCSVFile(File outputFile) {
        FileWriter mFileWriter = null;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile(), true);
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
            mCsvWriter.writeNext(new String[]{"Country", "City", "GalleryName", "Website", "NotSure"});
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot create empty output file");
        }
    }

    public void setOutputFile(File outputFolder, String placeHolder) {   //TODO: Set manually output folder
        if (!DirUtils.isDirOk(outputFolder)) {
        }
        this.outputFolder = outputFolder;
        String fileNameFromPlaceHolder = placeHolder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
        outputFile = new File(outputFolder + File.separator + fileNameFromPlaceHolder + ".csv");
    }

    public void saveResultCsvItems(ArrayList<OutputCsvModelItem> csvFileData) {
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }

        try {
            FileWriter writer = new FileWriter(outputFolder.getAbsoluteFile(), true);
            for (OutputCsvModelItem item : csvFileData) {
                //String[] data = new String[]{item.getCountry(), item.getCity(), item.getGalleryName(), item.getWebsite(), item.getNotSure()};
                //CSVUtils.writeLine(writer, Arrays.asList(data), ',', '"');
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot save data to output file");
        }
    }

    public <T extends GoogleSearchResultItem> ArrayList<OutputCsvModelItem> mapSearchResultsToOutputCSVModels(List<T> results) {
        ArrayList<OutputCsvModelItem> outputItems = new ArrayList<>();
        if (results.size() == 0) {
            return null;
        }
        for (GoogleSearchResultItem item : results) {
            //outputItems.add(new OutputCsvModelItem(item.getGalleryName(), item.getWebsite(), item.getCity(), item.getNotSureLink(), item.getCountry()));
        }
        return outputItems;
    }

    public void clearOutputFile() {
        outputFolder = null;
        outputFile = null;
    }

    public File getOutputFile() {
        return outputFile;
    }
}
