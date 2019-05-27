package Abstract.Commands;

import Services.DBConnectionService;
import Services.DIResolver;
import Services.GuiService;
import Utils.DirUtils;

import java.awt.event.ActionEvent;
import java.io.File;

public class ExportSettingsActionCommand  extends AbstractCommandAction {

    private final DIResolver diResolver;
    public ExportSettingsActionCommand(DIResolver diResolver) {
        super("Export settings");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiService guiService = diResolver.getGuiService();
        File settingsFile = DirUtils.saveFileDialog(guiService.getMainFrame(), "Select settings text file");

        if(settingsFile != null) {
            DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
            diResolver.getSettingsService().saveSearchSettingsToFile(dbConnectionService.getSearchSettings(), settingsFile);
        }
    }
}
