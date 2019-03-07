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

        String placeholderTerm = propertiesService.getPlaceHolder();
        String inputFile = new File(propertiesService.getInputFilePath()).getAbsolutePath();
        String settingsFile = new File(propertiesService.getSettingsFilePath()).getAbsolutePath();

        guiService.setInputFilePath(inputFile);
        guiService.setSettingsFilePath(settingsFile);
        guiService.setPlaceholder(placeholderTerm);

        outputDataService.setOutputFile(placeholderTerm);

        SearchingProcessor searchingProcessor = new SearchingProcessor();
        if (propertiesService.getWorkState()) {
            searchingProcessor.StartWork(guiService);
        }
    }
}
