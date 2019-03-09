package Abstract.Commands;

import Services.*;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;

public class ApplicationStartedActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public ApplicationStartedActionCommand(DIResolver diResolver) {
        super( "");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Application started");

        diResolver.getUserAgentsRotatorService().initList();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        InputDataService inputDataService = diResolver.getInputDataService();
        SettingsService settingsService = diResolver.getSettingsService();

        inputDataService.initInputDataFile(propertiesService.getInputFile());
        outputDataService.setOutputFile(propertiesService.getOutputFolderPath());
        settingsService.initSettingsFile(propertiesService.getSettingsFilePath());

        guiService.setInputFilePath(inputDataService.getInputDataFile());
        guiService.setSettingsFilePath(settingsService.getSettingsDataFile());
        guiService.setOutputFolder(outputDataService.getOutputFile());
        guiService.setPlaceholder( propertiesService.getPlaceHolder());

        SearchingProcessor searchingProcessor = new SearchingProcessor();
        if (propertiesService.getWorkState()) {
            searchingProcessor.StartWork(guiService);
        }
    }
}
