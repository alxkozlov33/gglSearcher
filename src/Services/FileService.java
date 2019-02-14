package Services;

import Models.*;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    private Path inputFilePath;
    private Path inputExceptionsFilePath;
    private Path outputFilePath;

    private File inputFile;
    private File inputExceptionsFile;
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

        if (!FilenameUtils.getExtension(path).equalsIgnoreCase("csv")) {
            logService.LogMessage("Selected input file has invalid format");
            return;
        }

        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return;
        }
        inputFilePath = Paths.get(path);
        inputFile = inFile;
        logService.LogMessage("Input data file initialized:: " + inputFile.getAbsolutePath());
        guiService.setInputFilePath(inputFilePath.toString());
    }

    public void setExceptionsFile(String restoredPath) {
        String path = null;
        if (restoredPath == null) {
            path = selectFolderDialog();
        } else {
            path = restoredPath;
        }

        if (!FilenameUtils.getExtension(path).equalsIgnoreCase("txt")) {
            logService.LogMessage("Selected exceptions file has invalid format");
            return;
        }

        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return;
        }
        inputExceptionsFilePath = Paths.get(path);
        inputExceptionsFile = inFile;
        logService.LogMessage("Exceptions file initialized: " + inputExceptionsFile.getAbsolutePath());
        guiService.setInputExceptionsFilePath(inputExceptionsFile.toString());
    }

    public ArrayList<InputCsvModelItem> InitCSVItems() {
        if (inputFilePath == null) {
            logService.LogMessage("Input data file path empty. Application cannot start.");
            return null;
        }
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
        } catch (Exception ex) {
            logService.LogMessage("Something wrong with input file");
            ex.printStackTrace();
        }
        return csvFileData;
    }

    public void RestoreFilesControl() {
        setUpInputFile(propertiesService.getInputFilePath());
        setExceptionsFile(propertiesService.getExceptionsFilePath());
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
                mCsvWriter.writeNext(new String[]{item.getCountry(), item.getCity(), item.getGalleryName(), item.getWebsite(), item.getNotSure()});
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
            mCsvWriter.writeNext(new String[]{"Country", "City", "GalleryName", "Website", "NotSure"});
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File GetInputFile() {
        return inputFile;
    }

    public File GetInputExceptionsFile() {
        return inputExceptionsFile;
    }

    public void setUpOutputFile(String placeholder) {
        if (StringUtils.isEmpty(placeholder)) {
            logService.LogMessage("Check search placeholder and input file. Application cannot start.");
            return;
        }
        String fileName = placeholder.replace("$", "").replace("{", "").replace("}", "").replace("*", "").replace("\"", "");
        String parentFile = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf(File.separator))
                + File.separator
                + fileName
                + "." +
                FilenameUtils.getExtension(inputFilePath.toString());

        outputFilePath = Paths.get(parentFile);
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
            outputItems.add(new OutputCsvModelItem(item.getGalleryName(), item.getWebsite(), item.getCity(), item.getNotSureLink(), item.getCountry()));
        }
        return outputItems;
    }

    public SearchExceptions initExceptionsKeywords() {
        if (inputExceptionsFilePath == null) {
            logService.LogMessage("Exceptions file path empty. Application cannot start.");
            return null;
        }
        SearchExceptions se = new SearchExceptions();
        se.domainExceptions = new ArrayList<>();
        se.URLExceptions = new ArrayList<>();
        se.metaTagsExceptions = new ArrayList<>();
        se.topLevelDomainsExceptions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(inputExceptionsFilePath, StandardCharsets.UTF_8);
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
            e.printStackTrace();
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
            buffer.add(lines.get(k));
        }
        return buffer;
    }
}
