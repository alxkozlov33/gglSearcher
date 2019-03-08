package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
import Services.SettingsService;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;
import java.io.File;

public class SelectSettingsFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public SelectSettingsFileActionCommand(DIResolver diResolver) {
        super("Choose new exceptions file");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input settings file button action performed");
        SettingsService settingsService = diResolver.getSettingsService();
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        settingsService.initSettingsFile(null);

        File settingsFileAbsolutePath = settingsService.getSettingsDataFile();
        guiService.setSettingsFilePath(settingsFileAbsolutePath);
        propertiesService.saveInputFilePath(settingsFileAbsolutePath.getAbsolutePath());
    }
}
