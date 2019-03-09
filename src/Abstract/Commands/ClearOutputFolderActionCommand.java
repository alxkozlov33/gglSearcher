package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.OutputDataService;
import Services.SettingsService;
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

        guiService.clearOutputFolderPath();
        outputDataService.clearOutputFile();
    }
}