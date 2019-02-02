package Controllers;

import Services.FileService;
import Services.GuiService;
import Services.LogService;
import Services.PropertiesService;

public class MainController {

    FileService fileService;
    GuiService guiService;
    LogService logService;
    PropertiesService propertiesService;

    public MainController(FileService fileService,
                          GuiService guiService,
                          LogService logService,
                          PropertiesService propertiesService) {

        this.fileService = fileService;
        this.guiService = guiService;
        this.logService = logService;
        this.propertiesService = propertiesService;
    }

    public void StartButtonClickAction() {
        System.out.println("Start action button pressed");
    }

    public void StopButtonClickAction() {
        System.out.println("Stop action button pressed");
    }

    public void SelectInputFile() {
        fileService.setUpInputFile();
    }
}
