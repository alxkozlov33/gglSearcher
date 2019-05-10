package Services;

import Abstract.Models.Database.Settings;
import Abstract.Models.Database.SettingsDao;
import Abstract.Models.SearchSettings;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.apache.commons.lang.StringUtils;
import org.tinylog.Logger;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collections;

public class DBConnectionService {

    private static JdbcPooledConnectionSource connectionSource;
    private final String delimiter = ";";

    public DBConnectionService() {
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:h2:"
                    + new File(DBConnectionService.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath()
                    + File.separator+"gglSearcherDb");
            createTableIfNotExists();
        } catch (SQLException | URISyntaxException e) {
            Logger.tag("SYSTEM").error(e);
        }
    }

    public void saveSearchSettings(SearchSettings searchSettings) {
        try {
           updatePropertyByKey(PropertyKeys.DomainExceptions, String.join(delimiter, searchSettings.domainExceptions ));
           updatePropertyByKey(PropertyKeys.KeywordsInSearchResults, String.join(delimiter, searchSettings.keywordsInSearchResults ));
           updatePropertyByKey(PropertyKeys.MetaTagsExceptions, String.join(delimiter, searchSettings.metaTagsExceptions ));
           updatePropertyByKey(PropertyKeys.SpecificWordsInDomainURLs, String.join(delimiter, searchSettings.specificWordsInDomainURLs ));
           updatePropertyByKey(PropertyKeys.TopLevelDomainsExceptions, String.join(delimiter, searchSettings.topLevelDomainsExceptions ));
           updatePropertyByKey(PropertyKeys.URLExceptions, String.join(delimiter, searchSettings.URLExceptions ));
        }
        catch(SQLException | NullPointerException ex) {
            Logger.tag("SYSTEM").error(ex);
        }
    }

    public SearchSettings getSearchSettings() {
        SearchSettings searchSettings = new SearchSettings();
        try {
            Collections.addAll(searchSettings.domainExceptions, getPropertyByKey(PropertyKeys.DomainExceptions).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.keywordsInSearchResults, getPropertyByKey(PropertyKeys.KeywordsInSearchResults).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.metaTagsExceptions, getPropertyByKey(PropertyKeys.MetaTagsExceptions).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.specificWordsInDomainURLs, getPropertyByKey(PropertyKeys.SpecificWordsInDomainURLs).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.topLevelDomainsExceptions, getPropertyByKey(PropertyKeys.TopLevelDomainsExceptions).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.URLExceptions, getPropertyByKey(PropertyKeys.URLExceptions).getSettingValue().split(delimiter));
        } catch (SQLException | NullPointerException ex) {
            Logger.error(ex);
        }

        return searchSettings;
    }

    private void createTableIfNotExists() throws SQLException {
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
    }

    private void updatePropertyByKey(PropertyKeys propertyKey, String value) throws SQLException {
        if (StringUtils.isEmpty(value) || propertyKey == null) {
            return;
        }
        SettingsDao settingsDao
                = DaoManager.createDao(connectionSource, Settings.class);

        Settings settings = getPropertyByKey(propertyKey);
        settings.setSettingName(propertyKey);
        settings.setSettingValue(value);

        settingsDao.update(settings);
    }

    private Settings getPropertyByKey(PropertyKeys propertyKey) throws SQLException {
        if (propertyKey == null) {
            return null;
        }
        SettingsDao settingsDao
                = DaoManager.createDao(connectionSource, Settings.class);
        Settings settings = settingsDao.findByKey(propertyKey).get(0);
        if (StringUtils.isEmpty(settings.getSettingValue())) {
            settings.setSettingValue("");
        }
        return settings;
    }
}
