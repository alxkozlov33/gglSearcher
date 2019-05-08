package Services;

import Abstract.Models.Database.Settings;
import Abstract.Models.Database.SettingsDao;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.tinylog.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class DBConnectionService {

    private static JdbcPooledConnectionSource connectionSource;

    public DBConnectionService() {
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:h2:~/gglSearcherDb");
            createTableIfNotExists();
        } catch (SQLException | IOException e) {
            Logger.error(e);
        }
    }

    private void createTableIfNotExists() throws SQLException, IOException {
            TableUtils.createTableIfNotExists(connectionSource, Settings.class);

            SettingsDao settingsDao
                    = DaoManager.createDao(connectionSource, Settings.class);

            Settings SpecificWordsInDomainURLs = new Settings();
            SpecificWordsInDomainURLs.setSettingName(PropertyKeys.SpecificWordsInDomainURLs);
            settingsDao.createIfNotExists(SpecificWordsInDomainURLs);

            Settings MetaTagsExceptions = new Settings();
            MetaTagsExceptions.setSettingName(PropertyKeys.MetaTagsExceptions);
            settingsDao.createIfNotExists(MetaTagsExceptions);

            Settings DomainExceptions = new Settings();
            DomainExceptions.setSettingName(PropertyKeys.DomainExceptions);
            settingsDao.createIfNotExists(DomainExceptions);

            Settings TopLevelDomainsExceptions = new Settings();
            TopLevelDomainsExceptions.setSettingName(PropertyKeys.TopLevelDomainsExceptions);
            settingsDao.createIfNotExists(TopLevelDomainsExceptions);

            Settings KeywordsInSearchResults = new Settings();
            KeywordsInSearchResults.setSettingName(PropertyKeys.KeywordsInSearchResults);
            settingsDao.createIfNotExists(KeywordsInSearchResults);

            Settings URLExceptions = new Settings();
            URLExceptions.setSettingName(PropertyKeys.URLExceptions);
            settingsDao.createIfNotExists(URLExceptions);

            connectionSource.close();
    }

    public void updatePropertyByKey(PropertyKeys propertyKey, String value) throws SQLException {
        SettingsDao settingsDao
                = DaoManager.createDao(connectionSource, Settings.class);

        Settings settings = new Settings();
        settings.setSettingName(PropertyKeys.TopLevelDomainsExceptions);

        settingsDao.createOrUpdate(settings);
    }
}
