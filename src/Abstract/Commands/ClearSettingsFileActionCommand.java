package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
import Services.SettingsService;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class ClearSettingsFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public ClearSettingsFileActionCommand(DIResolver diResolver) {
        super("Clear settings file");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Settings data file removed");
        SettingsService settingsService = diResolver.getSettingsService();
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        settingsService.clearSettingsFile();
        guiService.clearSettingsFilePath();
        propertiesService.saveSettingsFilePath(null);
    }
}
