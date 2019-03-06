package Abstract.Commands;

import Models.SearchSettings;
import Services.DIResolver;
import Services.SearchingProcessor;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class RunButtonActionCommand extends AbstractCommandAction {

    public RunButtonActionCommand(DIResolver diResolver) {
        super(diResolver,"Run");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        propertiesService.saveWorkState(true);
        propertiesService.saveInputFilePath(fileService.getInputFilePath());
        propertiesService.saveExceptionsFilePath(fileService.getExceptionsFilePath());
        propertiesService.savePlaceHolder(guiService.getSearchPlaceholderText());
        ArrayList inputCsvData = fileService.InitCSVItems();

        fileService.SetOutputFile(guiService.getSearchPlaceholderText());
        SearchSettings searchSettings = fileService.initSettingsFile();
        //searchService.DoWork(inputCsvData, searchSettings);

        SearchingProcessor searchingProcessor = new SearchingProcessor(propertiesService, fileService, guiService);

        Thread worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            searchingProcessor.setWorkFlagToRun();
            searchingProcessor.StartWork();
            guiService.changeApplicationStateToWork(false);
            logService.LogMessage("Finished");
            guiService.setStatusText("Finished...");
        });
        worker.start();
    }
}
