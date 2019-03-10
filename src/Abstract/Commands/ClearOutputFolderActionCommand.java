package Abstract.Commands;

import Services.*;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class ClearOutputFolderActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public ClearOutputFolderActionCommand(DIResolver diResolver) {
        super("Clear output folder");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Output folder removed");
        GuiService guiService = diResolver.getGuiService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        guiService.clearOutputFolderPath();
        outputDataService.clearOutputFile();
        propertiesService.saveOutputFolderPath(null);
    }
}