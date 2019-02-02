package Services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService {

    private Path inputFilePath;
    private Path outputFilePath;

    private File inputFile;
    private File outputFile;

    GuiService guiService;

    public FileService(GuiService guiService) {
        this.guiService = guiService;
    }

    public void setUpInputFile() {
        String path = selectFolderDialog();
        File inFile = new File(path);
        if (StringUtils.isEmpty(path) && !inFile.exists()) {
            return;
        }
        inputFilePath = Paths.get(path);
        inputFile = inFile;
        setUpOutputFile();
    }

    private void setUpOutputFile() {
        String basename = inputFile.getParent() + File.separator + FilenameUtils.getBaseName(inputFilePath.toString());
        String updatedOutputFilePath = basename+ " - updated." + FilenameUtils.getExtension(inputFilePath.toString());
        outputFilePath = Paths.get(updatedOutputFilePath);
        outputFile = outputFilePath.toFile();
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

    private String cutPath(String path) {
        int size = 70;
        if (path.length() <= size) {
            return path;
        } else if (path.length() > size) {
            return "..."+path.substring(path.length() - (size - 3));
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("Something wrong with file path cut");
        }
    }
}
