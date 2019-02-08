package Controllers;

import Services.*;

public class MainController {

    private FileService fileService;
    private GuiService guiService;
    private LogService logService;
    private PropertiesService propertiesService;
    private SearchService searchService;

    public MainController(FileService fileService,
                          GuiService guiService,
                          LogService logService,
                          PropertiesService propertiesService,
                          SearchService searchService) {

        this.fileService = fileService;
        this.guiService = guiService;
        this.logService = logService;
        this.propertiesService = propertiesService;
        this.searchService = searchService;
    }

    public void StartButtonClickAction() {
        System.out.println("Start action button pressed");
        propertiesService.saveWorkState(true);
        propertiesService.saveInputFilePath(fileService.GetInputFile());
        propertiesService.saveExceptionsFilePath(fileService.GetInputExceptionsFile());
        propertiesService.savePlaceHolder(guiService.getBootstrapper().getSearchingPlaceHolder().getText());
        searchService.Work();
    }

    public void StopButtonClickAction() {
        //System.out.println("Stop action button pressed");
        propertiesService.saveWorkState(false);
        propertiesService.saveIndex(0);
        logService.LogMessage("Stopping...");
        logService.UpdateStatus("Stopping...");
        searchService.setWorkStateToStop();
    }

    public void SelectInputFile() {
        fileService.setUpInputFile(null);
    }

    public void SelectExceptionsFile() {
        fileService.setExceptionsFile(null);
    }
}
