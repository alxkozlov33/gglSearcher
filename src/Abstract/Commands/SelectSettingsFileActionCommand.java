package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
import Services.SettingsService;
import java.awt.event.ActionEvent;

public class SelectSettingsFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public SelectSettingsFileActionCommand(DIResolver diResolver) {
        super("Choose new exceptions file");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SettingsService settingsService = diResolver.getSettingsService();
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        settingsService.initSettingsFile(null);

        String settingsFileAbsolutePath = settingsService.getSettingsDataFile().getAbsolutePath();
        guiService.setSettingsFilePath(settingsFileAbsolutePath);
        propertiesService.saveInputFilePath(settingsFileAbsolutePath);
    }
}
