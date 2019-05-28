package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class SelectApplicationSettingsActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public SelectApplicationSettingsActionCommand(DIResolver diResolver) {
        super("Preferences");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select application settings file button action performed");
        GuiService guiService = diResolver.getGuiService();
        guiService.createNewAppSettingsDialog(diResolver);
    }
}