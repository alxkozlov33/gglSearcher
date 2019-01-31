package Services;

public class ServicesManager {
    private FileService fileService;
    private LogService logService;
    private PropertiesService propertiesService;

    public ServicesManager() {

    }

    public FileService getFileService() {
        if (fileService == null) {
            fileService = new FileService();
        }
        return fileService;
    }

    public LogService getLogService() {
        if (logService == null) {
            logService = new LogService();
        }
        return logService;
    }

    public PropertiesService getPropertiesService() {
        if (propertiesService == null) {
            propertiesService = new PropertiesService();
        }
        return propertiesService;
    }
}
