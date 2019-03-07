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





//    public String getInputFilePath() {
//        if (inputFile == null){
//            return "";
//        }
//        return inputFile.toString();
//    }
//    public String getExceptionsFilePath() {
//        if (inputExceptionsFile == null){
//            return "";
//        }
//        return inputExceptionsFile.toString();
//    }



//    public ArrayList<OutputCsvModelItem> mapSearchResultsToOutputCSVModels(List<OutputRegularCSVItem> results) {
//        ArrayList<OutputCsvModelItem> outputItems = new ArrayList<>();
//        if (results.size() == 0) {
//            return null;
//        }
//        for (OutputRegularCSVItem item : results) {
//            outputItems.add(new OutputCsvModelItem(item.getGalleryName(), item.getWebsite(), item.getCity(), item.getNotSureLink(), item.getCountry()));
//        }
//        return outputItems;
//    }



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
