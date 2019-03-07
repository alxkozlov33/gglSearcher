package Services;

import Abstract.OutputModels.OutputRegularCSVItem;
import GUI.Bootstrapper;
import Models.*;
import Abstract.OutputModels.OutputCsvModelItem;
import Utils.CSVUtils;
import Utils.StrUtils;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.*;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileService {

    private File inputFile;
    private File inputExceptionsFile;
    private File outputFile;

    //private final GuiService guiService;
    //private final LogService logService;

    public FileService() {
       // this.guiService = DIResolver.getGuiService();
        //this.logService = DIResolver.getLogService();
    }

    public void clearInputFile() {
        inputFile = null;
    }
    public void clearExceptionsFile() {
        inputExceptionsFile = null;
    }


    public boolean SetExceptionsFile(String restoredPath) {
        String path;
        if (restoredPath == null) {
            path = selectFileDialog();
        } else {
            path = restoredPath;
        }

        if (!FilenameUtils.getExtension(path).equalsIgnoreCase("txt")) {
            Logger.info("Selected exceptions file has invalid format");
            return false;
        }

        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return false;
        }
        inputExceptionsFile = new File(path);
        Logger.info("Settings file initialized: " + inputExceptionsFile.getAbsolutePath());
        return true;
    }
    public void SetOutputFile(String placeholder) {
        if (StringUtils.isEmpty(placeholder)) {
            Logger.error("Check search placeholder and input file. Application cannot start.");
        }
        String fileName = placeholder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
        String parentFile = null;
        if (outputFile == null || inputFile == null) {
            String absolutePath = new File(".").getAbsolutePath();
            if(absolutePath.endsWith("."))
            {
                absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
            }
            String filePath = absolutePath + fileName + ".csv";
            outputFile = new File(filePath);
        }
        else {
            String str = FilenameUtils.getExtension(inputFile.getPath());
            parentFile = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf(File.separator))
                    + File.separator
                    + fileName
                    + "."
                    + str;
            outputFile = new File(parentFile);
        }

        try {
            if (!outputFile.exists()) {
                if (!outputFile.getParentFile().exists())
                    outputFile.getParentFile().mkdirs();
                if (!outputFile.exists())
                    outputFile.createNewFile();
                createEmptyCSVFile();
            }
        } catch (IOException e) {
            Logger.error(e,"Check search placeholder and input file. Application cannot start.");
        }
        Logger.info("Output file initialized: " + outputFile.getAbsolutePath());
    }

    public String getInputFilePath() {
        if (inputFile == null){
            return "";
        }
        return inputFile.toString();
    }
    public String getExceptionsFilePath() {
        if (inputExceptionsFile == null){
            return "";
        }
        return inputExceptionsFile.toString();
    }

    public void SaveResultCsvItems(ArrayList<OutputCsvModelItem> csvFileData) {
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }

        try {
            FileWriter writer = new FileWriter(outputFile.getAbsoluteFile(), true);
            for (OutputCsvModelItem item : csvFileData) {
                String[] data = new String[]{item.getCountry(), item.getCity(), item.getGalleryName(), item.getWebsite(), item.getNotSure()};
                CSVUtils.writeLine(writer, Arrays.asList(data), ',', '"');
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Logger.error(e, "Cannot save data to output file");
        }
    }

    public ArrayList<OutputCsvModelItem> mapSearchResultsToOutputCSVModels(List<OutputRegularCSVItem> results) {
        ArrayList<OutputCsvModelItem> outputItems = new ArrayList<>();
        if (results.size() == 0) {
            return null;
        }
        for (OutputRegularCSVItem item : results) {
            outputItems.add(new OutputCsvModelItem(item.getGalleryName(), item.getWebsite(), item.getCity(), item.getNotSureLink(), item.getCountry()));
        }
        return outputItems;
    }

    private void createEmptyCSVFile() {
        FileWriter mFileWriter = null;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile(), true);
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
            mCsvWriter.writeNext(new String[]{"Country", "City", "GalleryName", "Website", "NotSure"});
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            Logger.error(e, "Cannot create empty output file");
        }
    }

    private String selectFileDialog() {
        String osName = System.getProperty("os.name");
        String result = "";
        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(new Bootstrapper(), "Select file");
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            chooser.setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            if (chooser.getFile() != null) {
                result = chooser.getDirectory() + chooser.getFile();
            }
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select target file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = chooser.showDialog(new Bootstrapper(), "Select file");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                String folderName = userSelectedFolder.getAbsolutePath();
                result = folderName;
            }
        }
        return result;
    }
}
