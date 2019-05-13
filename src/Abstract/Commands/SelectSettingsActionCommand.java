package Abstract.Commands;

import Services.DBConnectionService;
import Services.DIResolver;
import Services.GuiService;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;

public class SelectSettingsActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public SelectSettingsActionCommand(DIResolver diResolver) {
        super("Search settings");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input settings file button action performed");
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
        GuiService guiService = diResolver.getGuiService();
        guiService.createNewSettingsDialog(diResolver);
        dbConnectionService.saveSearchSettings(guiService.getSettingsDialog().getSearchSettings());
    }
}
