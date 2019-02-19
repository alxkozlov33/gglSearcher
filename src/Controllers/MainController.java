package Controllers;

import Models.SearchExceptions;
import Services.*;

import java.util.ArrayList;

public class MainController {

    private FileService fileService;
    private GuiService guiService;
    private LogService logService;
    private PropertiesService propertiesService;
    private SearchService searchService;
    private UserAgentsRotatorService userAgentsRotatorService;

    public MainController() {

        this.fileService = DIResolver.getFileService();
        this.guiService = DIResolver.getGuiService();
        this.logService = DIResolver.getLogService();
        this.propertiesService = DIResolver.getPropertiesService();
        this.searchService = DIResolver.getSearchService();
        this.userAgentsRotatorService = DIResolver.getUserAgentsRotatorService();
    }

    public void ApplicationStarted() {
        userAgentsRotatorService.initList();
        SearchExceptions searchExceptions = null;
        ArrayList inputCsvData = null;

        String placeholderTerm = propertiesService.getPlaceHolder();
        String inputFile = propertiesService.getInputFilePath();
        if(fileService.SetInputFile(inputFile)) {
            inputCsvData = fileService.InitCSVItems();
            guiService.setInputFilePath(inputFile);
        }
        else {
            placeholderTerm = placeholderTerm.replaceAll("\\$\\{column[A-z]\\}", "");
        }

        guiService.setPlaceholder(placeholderTerm);
        fileService.SetOutputFile(placeholderTerm);

        String exceptionsFile = propertiesService.getExceptionsFilePath();
        if (fileService.SetExceptionsFile(exceptionsFile)) {
            searchExceptions = fileService.initExceptionsKeywords();
            guiService.setInputExceptionsFilePath(exceptionsFile);
        }

        if (propertiesService.getWorkState()) {
            searchService.DoWork(inputCsvData, searchExceptions);
        }
    }

    public void StartButtonClickAction() {
        propertiesService.saveWorkState(true);
        propertiesService.saveInputFilePath(fileService.getInputFilePath());
        propertiesService.saveExceptionsFilePath(fileService.getExceptionsFilePath());
        propertiesService.savePlaceHolder(guiService.getBootstrapper().getSearchingPlaceHolder().getText());
        ArrayList inputCsvData = fileService.InitCSVItems();

        SearchExceptions searchExceptions = fileService.initExceptionsKeywords();
        searchService.DoWork(inputCsvData, searchExceptions);
    }

    public void StopButtonClickAction() {
        propertiesService.saveWorkState(false);
        propertiesService.saveIndex(0);
        logService.LogMessage("Stopping...");
        logService.UpdateStatus("Stopping...");
        searchService.setWorkStateToStop();
    }

    public void SelectInputFile() {
        fileService.SetInputFile(null);
        guiService.setInputFilePath(fileService.getInputFilePath());
    }
    public void ClearInputFile() {
        fileService.clearInputFile();
        guiService.setInputFilePath("");
        guiService.setPlaceholder(guiService.getSearchPlaceholderText().replaceAll("\\$\\{column[A-z]\\}", ""));
    }
    public void SelectExceptionsFile() {
        fileService.SetExceptionsFile(null);
        guiService.setInputExceptionsFilePath(fileService.getExceptionsFilePath());
    }
    public void ClearExceptionsFile() {
        fileService.clearExceptionsFile();
        guiService.setInputExceptionsFilePath("");
    }
}
