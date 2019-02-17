package Services;

import Controllers.MainController;
import GUI.Bootstrapper;
import Services.*;

public class DIResolver {

    private static FileService fileService;
    private static LogService logService;
    private static PropertiesService propertiesService;
    private static GuiService guiService;
    private static SearchService searchService;
    private static ProxyService proxyService;
    private static UserAgentsRotatorService userAgentsRotatorService;

    private MainController mainController;
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
        mapActions();
        logService.LogMessage("Application started...");
    }
    public static GuiService getGuiService() {
        if (guiService == null) {
            guiService = new GuiService(bootstrapper);
        }
        return guiService;
    }

    public static SearchService getSearchService() {
        if (searchService == null) {
            searchService = new SearchService();
        }
        return searchService;
    }

    public static FileService getFileService() {
        if (fileService == null) {
            fileService = new FileService();
        }
        return fileService;
    }

    public static LogService getLogService() {
        if (logService == null) {
            logService = new LogService();
        }
        return logService;
    }

    public static PropertiesService getPropertiesService() {
        if (propertiesService == null) {
            propertiesService = new PropertiesService();
        }
        return propertiesService;
    }

    private MainController getMainController() {
        mainController = new MainController(fileService, guiService, logService, propertiesService, searchService);
        return mainController;
    }

    private void mapActions() {
        bootstrapper.getRunButton().addActionListener(e -> mainController.StartButtonClickAction());
        bootstrapper.getStopButton().addActionListener(e -> mainController.StopButtonClickAction());
        bootstrapper.getSelectFileButton().addActionListener(e -> mainController.SelectInputFile());
        bootstrapper.getSelectExceptionsFileButton().addActionListener(e -> mainController.SelectExceptionsFile());
    }

    public static ProxyService getProxyService() {
        if (proxyService == null) {
            proxyService = new ProxyService(logService);
        }
        return proxyService;
    }

    public static UserAgentsRotatorService getUserAgentsRotatorService() {
        if (userAgentsRotatorService == null) {
            userAgentsRotatorService = new UserAgentsRotatorService();
        }
        return userAgentsRotatorService;
    }
}
