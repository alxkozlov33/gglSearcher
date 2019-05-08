package Abstract.Commands;

import GUI.SettingsDialog;
import Services.DBConnectionService;
import Services.DIResolver;
import Utils.PropertyKeys;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class SelectSettingsFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public SelectSettingsFileActionCommand(DIResolver diResolver) {
        super("Choose new settings file");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input settings file button action performed");

        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setSize(500, 600);
        settingsDialog.setTitle("Settings");
        settingsDialog.setVisible(true);
        settingsDialog.setResizable(false);

        diResolver.getSettingsService().initSettingsData(settingsDialog.getSearchSettings());
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();

        try {
            dbConnectionService.updatePropertyByKey(PropertyKeys.DomainExceptions, String.join(";", settingsDialog.getSearchSettings().domainExceptions ));
            dbConnectionService.updatePropertyByKey(PropertyKeys.KeywordsInSearchResults, String.join(";", settingsDialog.getSearchSettings().keywordsInSearchResults ));
            dbConnectionService.updatePropertyByKey(PropertyKeys.MetaTagsExceptions, String.join(";", settingsDialog.getSearchSettings().metaTagsExceptions ));
            dbConnectionService.updatePropertyByKey(PropertyKeys.SpecificWordsInDomainURLs, String.join(";", settingsDialog.getSearchSettings().specificWordsInDomainURLs ));
            dbConnectionService.updatePropertyByKey(PropertyKeys.TopLevelDomainsExceptions, String.join(";", settingsDialog.getSearchSettings().topLevelDomainsExceptions ));
            dbConnectionService.updatePropertyByKey(PropertyKeys.URLExceptions, String.join(";", settingsDialog.getSearchSettings().URLExceptions ));
        }
        catch(SQLException ex) {
            Logger.error(ex);
        }
    }
}
