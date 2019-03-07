package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.InputDataService;
import Services.PropertiesService;

import java.awt.event.ActionEvent;

public class SelectInputDataFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        InputDataService inputDataService = diResolver.getInputDataService();

        inputDataService.initCSVItems(null); //TODO: Refactor
        String inputDataAbsolutePath = inputDataService.getInputDataFile().getAbsolutePath();
        guiService.setInputFilePath(inputDataAbsolutePath);
        propertiesService.saveInputFilePath(inputDataAbsolutePath);
    }

    public SelectInputDataFileActionCommand(DIResolver diResolver) {
        super("Choose new input file");
        this.diResolver = diResolver;
    }
}
