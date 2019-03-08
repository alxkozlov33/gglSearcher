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

        propertiesService.saveWorkState(true);
        propertiesService.saveInputFilePath(inputDataService.getInputDataFile().getAbsolutePath());
        propertiesService.saveExceptionsFilePath(settingsService.getSettingsDataFile().getAbsolutePath());
        propertiesService.savePlaceHolder(guiService.getSearchPlaceholderText());

        outputDataService.setOutputFile(guiService.getSearchPlaceholderText());
        SearchingProcessor searchingProcessor = new SearchingProcessor();

        Thread worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            searchingProcessor.StartWork(guiService);
            guiService.changeApplicationStateToWork(false);
            Logger.tag("SYSTEM").info("Finished");
            guiService.setStatusText("Finished...");
        });
        worker.start();
    }
}
