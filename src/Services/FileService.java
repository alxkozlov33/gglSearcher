package Services;

import Models.InputCsvModelItem;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        setUpOutputFile();
    }

    public ArrayList<InputCsvModelItem> InitCSVItems() {
        ArrayList csvFileData= null;
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
        }
        catch (Exception ex) {
            logService.LogMessage("Something wrong with input file");
            ex.printStackTrace();
        }
        return csvFileData;
    }

    public void RestoreFilesControl() {
        setUpInputFile(propertiesService.getInputFilePath());
    }

    public void saveCSVItems(ArrayList<InputCsvModelItem> csvFileData) {
        File outputFile = outputFilePath.toFile();
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(outputFile.getAbsolutePath()), StandardOpenOption.APPEND);
            StatefulBeanToCsv<InputCsvModelItem> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)

                    .build();
            beanToCsv.write(csvFileData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

    public File GetInputFile() {
        return inputFile;
    }

    private void setUpOutputFile() {
        String basename = inputFile.getParent() + File.separator + FilenameUtils.getBaseName(inputFilePath.toString());
        String updatedOutputFilePath = basename+ " - updated." + FilenameUtils.getExtension(inputFilePath.toString());
        outputFilePath = Paths.get(updatedOutputFilePath);
        outputFile = outputFilePath.toFile();
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
            chooser.setFileSelectionMode(chooser.FILES_ONLY);

            int returnVal = chooser.showDialog(guiService.getBootstrapper(), "Select file");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                String folderName = userSelectedFolder.getAbsolutePath();
                result = folderName;
            }
        }
        return result;
    }


}
