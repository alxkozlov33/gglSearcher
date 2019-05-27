package Abstract.Commands;

import Abstract.Models.SearchSettings;
import GUI.SettingsDialog;
import Services.DBConnectionService;
import Services.DIResolver;
import Services.GuiService;
import Services.SettingsService;
import Utils.DirUtils;

import java.awt.event.ActionEvent;
import java.io.File;

public class ImportSettingsActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;
    private final DBConnectionService dbConnectionService;
    private final SettingsService settingsService;

    public ImportSettingsActionCommand(DIResolver diResolver) {
        super("Import settings");
        this.diResolver = diResolver;
        this.dbConnectionService = diResolver.getDbConnectionService();
        this.settingsService = diResolver.getSettingsService();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiService guiService = diResolver.getGuiService();

        File settingsFile = DirUtils.selectFileDialog(guiService.getMainFrame(), "Select settings text file", "txt");
        if (DirUtils.isFileOk(settingsFile, "txt")) {
            SearchSettings searchSettings = settingsService.getSearchSettingsFromFile(settingsFile);
            dbConnectionService.saveSearchSettings(searchSettings);
            guiService.createNewSettingsDialog(diResolver);
        }
    }
}
