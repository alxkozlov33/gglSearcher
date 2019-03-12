package Abstract.Commands;

import Services.*;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class RunButtonActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public RunButtonActionCommand(DIResolver diResolver) {
        super("Run");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Run button action performed");

        OutputDataService outputDataService = diResolver.getOutputDataService();
        InputDataService inputDataService = diResolver.getInputDataService();
        SettingsService settingsService = diResolver.getSettingsService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();
        SearchService searchService = diResolver.getSearchService();

        propertiesService.saveWorkState(true);
        propertiesService.saveInputFilePath(inputDataService.getInputDataFile());
        propertiesService.saveSettingsFilePath(settingsService.getSettingsDataFile());
        propertiesService.savePlaceHolder(guiService.getSearchPlaceholderText());
        propertiesService.saveOutputFolderPath(outputDataService.getOutputFolder());


        Thread worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            searchService.StartWork(diResolver);
            guiService.changeApplicationStateToWork(false);
            Logger.tag("SYSTEM").info("Finished");
            guiService.setStatusText("Finished...");
        });
        worker.start();
    }
}
