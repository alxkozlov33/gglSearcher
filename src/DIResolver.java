import Controllers.MainController;
import GUI.Bootstrapper;
import Services.*;

public class DIResolver {

    private FileService fileService;
    private LogService logService;
    private PropertiesService propertiesService;
    private GuiService guiService;
    private SearchService searchService;
    private ProxyService proxyService;
    private UserAgentsRotatorService userAgentsRotatorService;

    private MainController mainController;
    private Bootstrapper bootstrapper;

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
    private GuiService getGuiService() {
        if (guiService == null) {
            guiService = new GuiService(bootstrapper, propertiesService);
        }
        return guiService;
    }

    private SearchService getSearchService() {
        if (searchService == null) {
            searchService = new SearchService(fileService, logService, guiService, propertiesService, proxyService, userAgentsRotatorService);
        }
        return searchService;
    }

    private FileService getFileService() {
        if (fileService == null) {
            fileService = new FileService(guiService, logService, propertiesService);
        }
        return fileService;
    }

    private LogService getLogService() {
        if (logService == null) {
            logService = new LogService(guiService);
        }
        return logService;
    }

    private PropertiesService getPropertiesService() {
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
