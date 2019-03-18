package Services;

import Utils.DirUtils;
import org.apache.commons.lang.StringUtils;
import org.tinylog.Logger;
import java.io.*;
import java.util.Properties;

public class PropertiesService {

    private File propertiesFile;
    private Properties properties;

    private String isWorkStateString = "isWorked";
    private String selectedCsvInputFileString = "selectedCsvInputFile";
    private String indexString = "index";
    private String placeholderPropertyString = "placeholder";
    private String selectedSettingsInputFileString = "settingsInputFile";

    public PropertiesService() {
        properties = new Properties();
        createNewFileIfNotExists();
    }

    private void saveProperty(String propertyName, String value) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertiesFile.getAbsoluteFile());
            properties.setProperty(propertyName, value);
            properties.store(output, null);
        } catch (IOException io) {
            Logger.tag("SYSTEM").error(io);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Logger.tag("SYSTEM").error(e);
                }
            }
        }
    }

    public void saveWorkState(boolean workState) {
        saveProperty(isWorkStateString, Boolean.toString(workState));
    }

    public void saveInputFilePath(File inputFile) {
        if (DirUtils.isFileOk(inputFile, "csv")) {
            saveProperty(selectedCsvInputFileString, inputFile.getAbsolutePath());
        } else {
            saveProperty(selectedCsvInputFileString, "");
        }
    }

    public void saveSettingsFilePath(File inputFile) {
        if (DirUtils.isFileOk(inputFile, "txt")) {
            saveProperty(selectedSettingsInputFileString, inputFile.getAbsolutePath());
        } else {
            saveProperty(selectedSettingsInputFileString, "");
        }
    }

    public void saveIndex(int index) {
        saveProperty(indexString, Integer.toString(index));
    }

    public void savePlaceHolder(String placeholder) {
        if (StringUtils.isEmpty(placeholder)) {
            saveProperty(placeholderPropertyString, "");
        } else {
            saveProperty(placeholderPropertyString, placeholder);
        }
    }

    public boolean getWorkState() {
        return Boolean.valueOf(restoreProperty(isWorkStateString));
    }
    public File getInputFile() {
        String restoredPath = restoreProperty(selectedCsvInputFileString);
        return new File(restoredPath);
    }
    public int getIndex() {
        String index = restoreProperty(indexString);
        if (StringUtils.isEmpty(index)) {
            return 0;
        }
        return Integer.parseInt(index);
    }
    public String getPlaceHolder() {
        return restoreProperty(placeholderPropertyString);
    }
    public File getSettingsFilePath() {
        String restoredPath = restoreProperty(selectedSettingsInputFileString);
        return new File(restoredPath);
    }

    private void createNewFileIfNotExists() {
        OutputStream output = null;
        try {
            //File propFile = new File(PropertiesService.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            File file = new File("configGGLS.properties");
            if (file.exists() && !file.isDirectory()) {
                propertiesFile = file;
            } else {
                propertiesFile = file;
                if (file.createNewFile()) {
                    output = new FileOutputStream(propertiesFile.getAbsoluteFile());
                    saveIndex(0);
                    saveWorkState(false);
                    saveInputFilePath(null);
                    saveSettingsFilePath(null);
                    savePlaceHolder(null);
                    properties.store(output, null);
                }
            }
        } catch (IOException io) {
            Logger.tag("SYSTEM").error(io);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Logger.tag("SYSTEM").error(e);
                }
            }
        }
    }

    private String restoreProperty(String propertyName) {
        String result = "";
        InputStream input = null;
        try {
            input = new FileInputStream(propertiesFile.getAbsoluteFile());
            properties.load(input);
            if (properties.get(propertyName) != null) {
                result = properties.get(propertyName).toString();
            }
        } catch (IOException ex) {
            Logger.tag("SYSTEM").error(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Logger.tag("SYSTEM").error(e);
                }
            }
        }
        return result;
    }
}
