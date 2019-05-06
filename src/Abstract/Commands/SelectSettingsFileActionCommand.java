package Abstract.Commands;

import GUI.SettingsDialog;
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
        SettingsService settingsService = diResolver.getSettingsService();

//        File settingsFile = DirUtils.selectFileDialog(guiService.getMainFrame(), "Select settings text file", "txt");
//        if (DirUtils.isFileOk(settingsFile, "txt")) {
//            guiService.setSettingsFilePath(settingsFile);
//            propertiesService.saveSettingsFilePath(settingsFile);
//            settingsService.initSettingsFile(settingsFile);
//        }

        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setSize(500, 600);
        settingsDialog.setTitle("Settings");
        settingsDialog.setVisible(true);
        settingsDialog.setResizable(false);
    }
}
