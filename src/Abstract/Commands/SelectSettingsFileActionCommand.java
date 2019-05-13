package Abstract.Commands;

import GUI.SettingsDialog;
import Services.DBConnectionService;
import Services.DIResolver;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;

public class SelectSettingsFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public SelectSettingsFileActionCommand(DIResolver diResolver) {
        super("Choose new settings file");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input settings file button action performed");
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();

        SettingsDialog settingsDialog = new SettingsDialog(diResolver);
        settingsDialog.setSize(500, 600);
        settingsDialog.setTitle("Settings");
        settingsDialog.setVisible(true);
        settingsDialog.setResizable(false);
        diResolver.getGuiService().setSettingsDialog(settingsDialog);

        dbConnectionService.saveSearchSettings(settingsDialog.getSearchSettings());
    }
}
