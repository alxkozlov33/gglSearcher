package Services;

import GUI.Bootstrapper;
import org.pmw.tinylog.Logger;

public class DIResolver {

    private FileService fileService;
    private LogService logService;
    private PropertiesService propertiesService;
    private GuiService guiService;
    private ProxyService proxyService;
    private UserAgentsRotatorService userAgentsRotatorService;
    private SearchingProcessor searchingProcessor;

    private static Bootstrapper bootstrapper;

    public DIResolver() {
    }

    public void initDependencies (Bootstrapper mainWindow) {
        bootstrapper = mainWindow;
        getUserAgentsRotatorService();
        getPropertiesService();
        getGuiService();
        getLogService();
        getProxyService();
        getFileService();
        getSearchingProcessor();

        Logger.info("Application started...");
    }
    public GuiService getGuiService() {
        if (guiService == null) {
            guiService = new GuiService(bootstrapper);
        }
        return guiService;
    }

    public SearchingProcessor getSearchingProcessor() {
        if (searchingProcessor == null) {
            searchingProcessor = new SearchingProcessor(propertiesService, fileService, guiService);
        }
        return searchingProcessor;
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

    public ProxyService getProxyService() {
        if (proxyService == null) {
            proxyService = new ProxyService();
        }
        return proxyService;
    }

    public UserAgentsRotatorService getUserAgentsRotatorService() {
        if (userAgentsRotatorService == null) {
            userAgentsRotatorService = new UserAgentsRotatorService();
        }
        return userAgentsRotatorService;
    }
}
