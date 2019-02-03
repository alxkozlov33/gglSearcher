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

    public PropertiesService() {
        properties = new Properties();
        createNewFile();
    }

    private void saveProperty(String propertyName, String value) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertiesFile.getAbsoluteFile());
            properties.setProperty(propertyName, value);
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveWorkState(boolean workState) {
        saveProperty(isWorkStateString, Boolean.toString(workState));
    }

    public void saveInputFilePath(File inputFile) {
        if (inputFile != null){
            saveProperty(selectedCsvInputFileString, inputFile.getAbsolutePath());
        }
        else {
            saveProperty(selectedCsvInputFileString, "");
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

    private void createNewFile() {
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
                savePlaceHolder(null);
                properties.store(output, null);
            }
            propertiesFileTemp.delete();
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private String restoreProperty(String propertyName) {
        String result = "0";
        InputStream input = null;
        try {
            createNewFile();
            input = new FileInputStream(propertiesFile.getAbsoluteFile());
            properties.load(input);
            if (properties.get(propertyName) != null) {
                result = properties.get(propertyName).toString();
            }
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getStackTrace());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
