import Controllers.MainController;
import GUI.Bootstrapper;
import Services.FileService;
import Services.GuiService;
import Services.LogService;
import Services.PropertiesService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DIResolver {

    private FileService fileService;
    private LogService logService;
    private PropertiesService propertiesService;
    private GuiService guiService;

    private MainController mainController;
    private Bootstrapper bootstrapper;

    public DIResolver() {
    }

    public void initDependencies (Bootstrapper mainWindow) {
        bootstrapper = mainWindow;
        getGuiService();
        getFileService();
        getLogService();
        getPropertiesService();

        getMainController();
        mapActions();
    }
    private GuiService getGuiService() {
        if (guiService == null) {
            guiService = new GuiService(bootstrapper);
        }
        return guiService;
    }

    private FileService getFileService() {
        if (fileService == null) {
            fileService = new FileService(guiService);
        }
        return fileService;
    }

    private LogService getLogService() {
        if (logService == null) {
            logService = new LogService();
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
        mainController = new MainController(fileService, guiService, logService, propertiesService);
        return mainController;
    }

    private void mapActions() {
        bootstrapper.getRunButton().addActionListener(e -> mainController.StartButtonClickAction());
        bootstrapper.getStopButton().addActionListener(e -> mainController.StopButtonClickAction());
        bootstrapper.getSelectFileButton().addActionListener(e -> mainController.SelectInputFile());
    }
}
