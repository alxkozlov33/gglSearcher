package Services;

import Models.InputCsvModelItem;
import Models.OutputCsvModelItem;
import Models.SearchResult;
import Models.SearchResultItem;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileService {

    private Path inputFilePath;
    private Path outputFilePath;

    private File inputFile;
    private File outputFile;

    private final GuiService guiService;
    private final LogService logService;
    private final PropertiesService propertiesService;

    public FileService(GuiService guiService, LogService logService, PropertiesService propertiesService) {
        this.guiService = guiService;
        this.logService = logService;
        this.propertiesService = propertiesService;
    }

    public void setUpInputFile(String restoredPath) {
        String path = null;
        if (restoredPath == null) {
            path = selectFolderDialog();
        } else {
            path = restoredPath;
        }

        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return;
        }
        inputFilePath = Paths.get(path);
        inputFile = inFile;
        logService.LogMessage("setUpInputFile: " + inputFile.getAbsolutePath());
    }

    public ArrayList<InputCsvModelItem> InitCSVItems() {
        ArrayList csvFileData = null;
        try {
            csvFileData = new ArrayList<InputCsvModelItem>();
            Reader reader = Files.newBufferedReader(inputFilePath);
            CsvToBean<InputCsvModelItem> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(InputCsvModelItem.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            csvFileData.addAll(IteratorUtils.toList(csvToBean.iterator()));
            reader.close();
            logService.LogMessage("CSV file was initialized");
        } catch (Exception ex) {
            logService.LogMessage("Something wrong with input file");
            ex.printStackTrace();
        }
        return csvFileData;
    }

    public void RestoreFilesControl() {
        setUpInputFile(propertiesService.getInputFilePath());
    }

    public void saveCSVItems(ArrayList<OutputCsvModelItem> csvFileData) {
        File outputFile = outputFilePath.toFile();
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }
        FileWriter mFileWriter = null;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile(), true);
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);

            for (OutputCsvModelItem item : csvFileData) {
                mCsvWriter.writeNext(new String[]{item.getAddress(), item.getGalleryName(), item.getWebsite()});
            }
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createEmptyCSVFile() {
        File outputFile = outputFilePath.toFile();
        FileWriter mFileWriter = null;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile(), true);
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
            mCsvWriter.writeNext(new String[]{"City", "GalleryName", "Website", "NotSure"});
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File GetInputFile() {
        return inputFile;
    }

    public void setUpOutputFile(String placeholder) {
        String resultString = placeholder.replace("$", "").replace("{", "").replace("}", "");
        String updatedOutputFilePath = inputFile.getParent() + File.separator +resultString+"." + FilenameUtils.getExtension(inputFilePath.toString());
        outputFilePath = Paths.get(updatedOutputFilePath);
        outputFile = outputFilePath.toFile();
        try {
            if (!outputFile.exists()) {
                if (!outputFile.getParentFile().exists())
                    outputFile.getParentFile().mkdirs();
                if (!outputFile.exists())
                    outputFile.createNewFile();
                createEmptyCSVFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logService.LogMessage("setUpOutputFile: " + outputFile.getAbsolutePath());
        guiService.setInputFilePath(inputFilePath.toString());
    }

    private String selectFolderDialog() {
        String osName = System.getProperty("os.name");
        String result = "";
        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(guiService.getBootstrapper(), "Select file");
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            chooser.setVisible(true);

            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            if (chooser.getFile() != null) {
                String fileName = chooser.getFile();
                result = fileName;
            }
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select target file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = chooser.showDialog(guiService.getBootstrapper(), "Select file");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                String folderName = userSelectedFolder.getAbsolutePath();
                result = folderName;
            }
        }
        return result;
    }

    public ArrayList<OutputCsvModelItem> mapSearchResultsToOutputCSVModels(SearchResult results) {
        ArrayList<OutputCsvModelItem> outputItems = new ArrayList<>();
        if (results.getResults().size() == 0) {
            return null;
        }
        for (SearchResultItem item : results.getResults()) {
            outputItems.add(new OutputCsvModelItem(item.getGalleryName(), item.getWebsite(), item.getAddress()));
        }
        return outputItems;
    }
}
