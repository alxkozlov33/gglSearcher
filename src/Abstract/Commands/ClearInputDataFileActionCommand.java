package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.InputDataService;
import Services.PropertiesService;
import Utils.StrUtils;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class ClearInputDataFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;
    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Input data file removed");
        GuiService guiService = diResolver.getGuiService();
        InputDataService inputDataService = diResolver.getInputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        inputDataService.clearInputDataFile();
        guiService.clearInputDataFilePath();
        propertiesService.saveInputFilePath(null);
    }

    public ClearInputDataFileActionCommand(DIResolver diResolver) {
        super("Clear existing file");
        this.diResolver = diResolver;
    }
}
