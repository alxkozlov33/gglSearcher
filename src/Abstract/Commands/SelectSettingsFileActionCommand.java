package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
import Services.SettingsService;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;
import java.io.File;

public class SelectSettingsFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public SelectSettingsFileActionCommand(DIResolver diResolver) {
        super("Choose new settings file");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input settings file button action performed");
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        SettingsService settingsService = new SettingsService();

        File settingsFile = DirUtils.selectFileDialog(guiService.getMainFrame(), "Select settings text file", "txt");
        if (DirUtils.isFileOk(settingsFile, "txt")) {
            guiService.setSettingsFilePath(settingsFile);
            propertiesService.saveSettingsFilePath(settingsFile);
            settingsService.initSettingsFile(settingsFile);
            //Thread worker = new Thread(() -> settingsService.initSettingsFileData(settingsFile));
            //worker.start();
        }
    }
}
