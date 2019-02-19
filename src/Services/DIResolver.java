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

    public static MainController getMainController() {
        if(mainController == null) {
            mainController = new MainController();
        }
        return mainController;
    }

    private void mapActions() {
        bootstrapper.getRunButton().addActionListener(e -> mainController.StartButtonClickAction());
        bootstrapper.getStopButton().addActionListener(e -> mainController.StopButtonClickAction());

        bootstrapper.getChooseExceptionsFile().addActionListener(e -> mainController.SelectExceptionsFile());
        bootstrapper.getClearExceptionsFile().addActionListener(e -> mainController.ClearExceptionsFile());

        bootstrapper.getChooseInputFile().addActionListener(e -> mainController.SelectInputFile());
        bootstrapper.getClearInputFile().addActionListener(e -> mainController.ClearInputFile());
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
