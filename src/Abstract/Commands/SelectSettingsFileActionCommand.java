package Abstract.Commands;

import Services.DBConnectionService;
import Services.DIResolver;
import Services.GuiService;
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
        GuiService guiService = diResolver.getGuiService();
        guiService.createNewSettingsDialog(diResolver);
        dbConnectionService.saveSearchSettings(guiService.getSettingsDialog().getSearchSettings());
    }
}
