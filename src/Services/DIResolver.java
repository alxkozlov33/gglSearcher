package Services;

import Controllers.MainController;
import GUI.Bootstrapper;
import org.pmw.tinylog.Logger;

public class DIResolver {

    private static FileService fileService;
    private static LogService logService;
    private static PropertiesService propertiesService;
    private static GuiService guiService;
    private static SearchService searchService;
    private static ProxyService proxyService;
    private static UserAgentsRotatorService userAgentsRotatorService;

    private static MainController mainController;
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
        getSearchService();

        getMainController();
        Logger.info("Application started...");
    }
    public GuiService getGuiService() {
        if (guiService == null) {
            guiService = new GuiService(bootstrapper);
        }
        return guiService;
    }

    public SearchService getSearchService() {
        if (searchService == null) {
            searchService = new SearchService();
        }
        return searchService;
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

    public MainController getMainController() {
        if(mainController == null) {
            mainController = new MainController();
        }
        return mainController;
    }

    public ProxyService getProxyService() {
        if (proxyService == null) {
            proxyService = new ProxyService(logService);
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
