package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.SettingsService;

import java.awt.event.ActionEvent;

public class ClearSettingsFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public ClearSettingsFileActionCommand(DIResolver diResolver) {
        super("Clear settings file");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SettingsService settingsService = diResolver.getSettingsService();
        GuiService guiService = diResolver.getGuiService();

        settingsService.clearSettingsFile();
        guiService.setSettingsFilePath("");
    }
}
