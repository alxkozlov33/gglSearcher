package Services;

import Models.*;
import Utils.DirUtils;
import Utils.StrUtils;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    private File inputFile;
    private File inputExceptionsFile;
    private File outputFile;

    private final GuiService guiService;
    private final LogService logService;

    public FileService() {
        this.guiService = DIResolver.getGuiService();
        this.logService = DIResolver.getLogService();
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
            path = DirUtils.selectFolderDialog(guiService.getBootstrapper());
        } else {
            path = restoredPath;
        }

        if (!FilenameUtils.getExtension(path).equalsIgnoreCase("csv")) {
            logService.LogMessage("Selected input file has invalid format or file not selected");
            return false;
        }

        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return false;
        }

        inputFile = inFile;
        logService.LogMessage("Input data file initialized: " + inputFile.getAbsolutePath());
        return true;
    }
    public boolean SetExceptionsFile(String restoredPath) {
        String path = null;
        if (restoredPath == null) {
            path = DirUtils.selectFolderDialog(guiService.getBootstrapper());
        } else {
            path = restoredPath;
        }

        if (!FilenameUtils.getExtension(path).equalsIgnoreCase("txt")) {
            logService.LogMessage("Selected exceptions file has invalid format");
            return false;
        }

        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return false;
        }
        inputExceptionsFile = inFile;
        logService.LogMessage("Exceptions file initialized: " + inputExceptionsFile.getAbsolutePath());
        return true;
    }
    public boolean SetOutputFile(String placeholder) {
        if (StringUtils.isEmpty(placeholder)) {
            logService.LogMessage("Check search placeholder and input file. Application cannot start.");
            return false;
        }
        String fileName = placeholder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
        String parentFile = null;
        if (outputFile == null || inputFile == null) {
            File f = new File(".");
            String filePath = f.getAbsolutePath().replace(".", "") + fileName;
            outputFile = new File(filePath);
        }
        else {
            parentFile = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf(File.separator))
                    + File.separator
                    + fileName
                    + "." +
                    FilenameUtils.getExtension(inputFile.getPath());
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
            logService.LogMessage("Check search placeholder and input file. Application cannot start.");
            logService.LogMessage(e.getMessage());
        }
        logService.LogMessage("Output file initialized: " + outputFile.getAbsolutePath());
        return true;
    }

    public String getInputFilePath() {
        if (inputFile == null){
            return "";
        }
        return inputFile.getAbsolutePath();
    }
    public String getExceptionsFilePath() {
        if (inputExceptionsFile == null){
            return "";
        }
        return inputExceptionsFile.getAbsolutePath();
    }

    public ArrayList<InputCsvModelItem> InitCSVItems() {
        if (inputFile == null) {
            logService.LogMessage("Input data file path empty.");
            return null;
        }
        if (StrUtils.isStringContainsExtraSymbols(inputFile.getAbsolutePath())) {
            logService.LogMessage("Input data file has wrong symbols in name or path");
            return null;
        }
        ArrayList csvFileData = null;
        try {
            csvFileData = new ArrayList<InputCsvModelItem>();
            Reader reader = Files.newBufferedReader(inputFile.toPath());
            CsvToBean<InputCsvModelItem> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(InputCsvModelItem.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            csvFileData.addAll(IteratorUtils.toList(csvToBean.iterator()));
            reader.close();
        } catch (Exception ex) {
            logService.LogMessage("Something wrong with input file");
            logService.LogMessage(ex.getMessage());
        }
        return csvFileData;
    }
    public void SaveResultCsvItems(ArrayList<OutputCsvModelItem> csvFileData) {
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }
        FileWriter mFileWriter = null;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile(), true);
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
            for (OutputCsvModelItem item : csvFileData) {
                mCsvWriter.writeNext(new String[]{item.getCountry(), item.getCity(), item.getGalleryName(), item.getWebsite(), item.getNotSure()});
            }
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            logService.LogMessage("Cannot save data to output file");
            logService.LogMessage(e.getMessage());
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
    public SearchExceptions initExceptionsKeywords() {
        if (inputExceptionsFile == null) {
            logService.LogMessage("Exceptions file path empty. Application cannot start.");
            return null;
        }
        if (StrUtils.isStringContainsExtraSymbols(inputExceptionsFile.getAbsolutePath())) {
            logService.LogMessage("Exceptions file has wrong symbols in name or path");
            return null;
        }
        SearchExceptions se = new SearchExceptions();
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
            logService.LogMessage("Cannot initialize input exceptions file");
            logService.LogMessage(e.getMessage());
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
            buffer.add(lines.get(k).replaceAll("\\s+","").toLowerCase());
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
            logService.LogMessage("Cannot create empty output file");
            logService.LogMessage(e.getMessage());
        }
    }
}
