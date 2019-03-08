package Services;

import Abstract.OutputModels.OutputCsvModelItem;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputDataService {

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

    public void setOutputFile(String placeholder) {   //TODO: Set manually output folder
        if (StringUtils.isEmpty(placeholder)) {
            Logger.tag("SYSTEM").error("Check search placeholder and input file. Application cannot start.");
            
        }
//        String fileName = placeholder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
//        String parentFile = null;
//        if (outputFile == null || inputFile == null) {
//            String absolutePath = new File(".").getAbsolutePath();
//            if(absolutePath.endsWith("."))
//            {
//                absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
//            }
//            String filePath = absolutePath + fileName + ".csv";
//            outputFile = new File(filePath);
//        }
//        else {
//            String str = FilenameUtils.getExtension(inputFile.getPath());
//            parentFile = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf(File.separator))
//                    + File.separator
//                    + fileName
//                    + "."
//                    + str;
//            outputFile = new File(parentFile);
//        }
//
//        try {
//            if (!outputFile.exists()) {
//                if (!outputFile.getParentFile().exists())
//                    outputFile.getParentFile().mkdirs();
//                if (!outputFile.exists())
//                    outputFile.createNewFile();
//                createEmptyCSVFile();
//            }
//        } catch (IOException e) {
//            Logger.error(e,"Check search placeholder and input file. Application cannot start.");
//        }
        //Logger.info("Output file initialized: " + outputFile.getAbsolutePath());
    }

    public void saveResultCsvItems(ArrayList<OutputCsvModelItem> csvFileData) {
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }

        try {
            FileWriter writer = new FileWriter(outputFile.getAbsoluteFile(), true);
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
}
