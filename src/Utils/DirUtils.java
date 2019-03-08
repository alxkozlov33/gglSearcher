package Utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DirUtils {
    public static File selectFileDialog(Frame frame) {
        String osName = System.getProperty("os.name");
        File result = null;
        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(frame, "Select file");
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            chooser.setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            if (chooser.getFile() != null) {
                result = new File(chooser.getDirectory() + chooser.getFile());
            }
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = chooser.showDialog(frame, "Select file");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                result = new File(userSelectedFolder.getAbsolutePath());
            }
        }
        return result;
    }

    public static boolean isFileOk(File file, String extension) {
        if (file == null || StringUtils.isEmpty(file.getAbsolutePath()) || !file.exists() || !FilenameUtils.isExtension(file.getAbsolutePath(), extension)) {
            return false;
        }
        return true;
    }
}
