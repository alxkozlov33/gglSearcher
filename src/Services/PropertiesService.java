package Services;

import java.io.*;
import java.util.Properties;

public class PropertiesService {

    private File propertiesFile;
    private Properties properties;

    private String isWorkStateString = "isWorked";
    private String selectedCsvInputFileString = "selectedCsvInputFile";
    private String indexString = "index";
    private String placeholderPropertyString = "placeholder";
    private String selectedExceptionsInputFileString = "exceptionsInputFile";

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
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void saveWorkState(boolean workState) {
        saveProperty(isWorkStateString, Boolean.toString(workState));
    }

    public void saveInputFilePath(String inputFile) {
        if (inputFile != null){
            saveProperty(selectedCsvInputFileString, inputFile);
        }
        else {
            saveProperty(selectedCsvInputFileString, "");
        }
    }
    public void saveExceptionsFilePath(String inputFile) {
        if (inputFile != null){
            saveProperty(selectedExceptionsInputFileString, inputFile);
        }
        else {
            saveProperty(selectedExceptionsInputFileString, "");
        }
    }
    public void saveIndex(int index) {
        saveProperty(indexString, Integer.toString(index));
    }
    public void savePlaceHolder(String placeholder) {
        if (placeholder != null) {
            saveProperty(placeholderPropertyString, placeholder);
        }
        else {
            saveProperty(placeholderPropertyString, "");
        }
    }

    public boolean getWorkState() {
        return Boolean.valueOf(restoreProperty(isWorkStateString));
    }
    public String getInputFilePath() {
        return restoreProperty(selectedCsvInputFileString);
    }
    public int getIndex() {
        return Integer.parseInt(restoreProperty(indexString));
    }
    public String getPlaceHolder() {
        return restoreProperty(placeholderPropertyString);
    }
    public String getExceptionsFilePath() {
        return restoreProperty(selectedExceptionsInputFileString);
    }

    private void createNewFileIfNotExists() {
        OutputStream output = null;
        try {
            File propertiesFileTemp = File.createTempFile("configGGLS", ".properties");
            String propPath = propertiesFileTemp.getAbsolutePath().substring(0, propertiesFileTemp.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + "configGGLS.properties";
            File f = new File(propPath);
            if (f.exists() && !f.isDirectory()) {
                propertiesFile = f;
            } else {
                propertiesFile = f;
                f.createNewFile();
                output = new FileOutputStream(propertiesFile.getAbsoluteFile());
                saveIndex(0);
                saveWorkState(false);
                saveInputFilePath(null);
                saveExceptionsFilePath(null);
                savePlaceHolder(null);
                properties.store(output, null);
            }
            propertiesFileTemp.delete();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private String restoreProperty(String propertyName) {
        String result = "";
        InputStream input = null;
        try {
            createNewFileIfNotExists();
            input = new FileInputStream(propertiesFile.getAbsoluteFile());
            properties.load(input);
            if (properties.get(propertyName) != null) {
                result = properties.get(propertyName).toString();
            }
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getStackTrace());
                }
            }
        }
        return result;
    }
}
