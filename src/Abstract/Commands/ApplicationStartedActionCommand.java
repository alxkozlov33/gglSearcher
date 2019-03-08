package Abstract.Commands;

import Services.*;
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
        diResolver.getUserAgentsRotatorService().initList();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        InputDataService inputDataService = diResolver.getInputDataService();
        SettingsService settingsService = diResolver.getSettingsService();

        String placeholderTerm = propertiesService.getPlaceHolder();

        inputDataService.initCSVItems(propertiesService.getInputFilePath());
        settingsService.initSettingsFile(propertiesService.getSettingsFilePath());

        guiService.setInputFilePath(inputDataService.getInputDataFile().getAbsolutePath());
        guiService.setSettingsFilePath(settingsService.getSettingsDataFile().getAbsolutePath());
        guiService.setPlaceholder(placeholderTerm);

        outputDataService.setOutputFile(placeholderTerm);

        SearchingProcessor searchingProcessor = new SearchingProcessor();
        if (propertiesService.getWorkState()) {
            searchingProcessor.StartWork(guiService);
        }
    }
}
