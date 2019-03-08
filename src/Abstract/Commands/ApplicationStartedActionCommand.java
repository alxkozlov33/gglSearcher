package Abstract.Commands;

import Services.*;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;
import java.io.File;

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

        String placeholderTerm = propertiesService.getPlaceHolder();

        inputDataService.initInputDataFile(propertiesService.getInputFilePath());
        settingsService.initSettingsFile(propertiesService.getSettingsFilePath());

        guiService.setInputFilePath(inputDataService.getInputDataFile());
        guiService.setSettingsFilePath(settingsService.getSettingsDataFile());
        guiService.setPlaceholder(placeholderTerm);

        outputDataService.setOutputFile(placeholderTerm);

        SearchingProcessor searchingProcessor = new SearchingProcessor();
        if (propertiesService.getWorkState()) {
            searchingProcessor.StartWork(guiService);
        }
    }
}
