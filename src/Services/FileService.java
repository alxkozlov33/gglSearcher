package Services;

import GUI.Bootstrapper;
import Models.*;
import Models.OutputCSVModels.OutputCsvModelItem;
import Models.SearchResultsModels.SearchResult;
import Models.SearchResultsModels.SearchResultItem;
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

    public boolean SetInputFile(String restoredPath) {
        String path;
        if (restoredPath == null) {
            path = selectFileDialog();
        } else {
            path = restoredPath;
        }

        if (!FilenameUtils.getExtension(path).equalsIgnoreCase("csv")) {
            Logger.info("Selected input file has invalid format or file not selected");
            return false;
        }

        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return false;
        }

        inputFile = new File(path);
        Logger.info("Input data file initialized: "+ inputFile);
        return true;
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

    public ArrayList<InputCsvModelItem> InitCSVItems() {
        if (inputFile == null) {
            Logger.info("Input data file path empty");
            return null;
        }
        if (StrUtils.isStringContainsExtraSymbols(inputFile.getAbsolutePath())) {
            Logger.info("Input data file has wrong symbols in name or path");
            return null;
        }
        ArrayList csvFileData = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(inputFile.getAbsolutePath()));
            CsvToBean<InputCsvModelItem> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(InputCsvModelItem.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            csvFileData = new ArrayList(IteratorUtils.toList(csvToBean.iterator()));
            reader.close();
        } catch (Exception ex) {
            Logger.error(ex, "Something wrong with input file");
        }
        return csvFileData;
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

    public ArrayList<OutputCsvModelItem> mapSearchResultsToOutputCSVModels(SearchResult results) {
        ArrayList<OutputCsvModelItem> outputItems = new ArrayList<>();
        if (results.getResults().size() == 0) {
            return null;
        }
        for (SearchResultItem item : results.getResults()) {
            outputItems.add(new OutputCsvModelItem(item.getGalleryName(), item.getWebsite(), item.getCity(), item.getNotSureLink(), item.getCountry()));
        }
        return outputItems;
    }
    public SearchSettings initSettingsFile() {
        if (inputExceptionsFile == null) {
            Logger.info("Exceptions file path empty");
            return null;
        }
        if (StrUtils.isStringContainsExtraSymbols(inputExceptionsFile.getAbsolutePath())) {
            Logger.info("Exceptions file has wrong symbols in name or path");
            return null;
        }
        SearchSettings se = new SearchSettings();
        se.domainExceptions = new ArrayList<>();
        se.URLExceptions = new ArrayList<>();
        se.metaTagsExceptions = new ArrayList<>();
        se.topLevelDomainsExceptions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(inputExceptionsFile.toPath(), StandardCharsets.UTF_8);
            lines.removeIf(l -> l.equals(""));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("# Exceptions for found domains:")) {
                    se.domainExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for words in domain URLs:")) {
                    se.URLExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions meta titles:")) {
                    se.metaTagsExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for top level domains:")) {
                    se.topLevelDomainsExceptions = new ArrayList<>(collectTerms(i, lines));
                }
            }
        } catch (IOException e) {
            Logger.error(e,"Cannot initialize input exceptions file");
        }
        return se;
    }

    private ArrayList<String> collectTerms(int index, List<String> lines) {
        ArrayList<String> buffer = new ArrayList<>();
        for (int k = (index+1); k < lines.size(); k++)
        {
            if (lines.get(k).startsWith("#")) {
                break;
            }
            buffer.add(lines.get(k).trim().toLowerCase());
        }
        return buffer;
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
