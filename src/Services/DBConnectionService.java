package Services;

import Abstract.Models.Database.DAO.AppSettingsDao;
import Abstract.Models.Database.Entities.ApplicationSettingsEntity;
import Abstract.Models.Database.Entities.SearchSettingsEntity;
import Abstract.Models.Database.DAO.SearchSettingsDao;
import Abstract.Models.SearchSettings;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.apache.commons.lang.StringUtils;
import org.tinylog.Logger;
import java.io.File;
import java.sql.SQLException;
import java.util.Collections;

public class DBConnectionService {

    private static JdbcPooledConnectionSource connectionSource;
    private final String delimiter = ";";

    public DBConnectionService() {
        try {
            String dbPath = "jdbc:h2:"
                    + new File(DBConnectionService.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath().replace(".jar", "")
                    + File.separator+"gglSearcherDb";
            Logger.tag("SYSTEM").info(dbPath);
            connectionSource = new JdbcPooledConnectionSource(dbPath);
            createTablesIfNotExists();
            initFillSearchSettingsTable();
            initFillApplicationSettingsTable();
        } catch (Exception e) {
            Logger.tag("SYSTEM").error(e);
        }
    }

    private void createTablesIfNotExists() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, SearchSettingsEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ApplicationSettingsEntity.class);
    }

    private void initFillSearchSettingsTable() throws SQLException {
        SearchSettingsDao searchSettingsDao = DaoManager.createDao(connectionSource, SearchSettingsEntity.class);

        SearchSettingsEntity SpecificWordsInDomainURLs = new SearchSettingsEntity();
        SpecificWordsInDomainURLs.setSettingName(PropertyKeys.SpecificWordsInDomainURLs);
        searchSettingsDao.createIfNotExists(SpecificWordsInDomainURLs);

        SearchSettingsEntity MetaTagsExceptions = new SearchSettingsEntity();
        MetaTagsExceptions.setSettingName(PropertyKeys.MetaTagsExceptions);
        searchSettingsDao.createIfNotExists(MetaTagsExceptions);

        SearchSettingsEntity DomainExceptions = new SearchSettingsEntity();
        DomainExceptions.setSettingName(PropertyKeys.DomainExceptions);
        searchSettingsDao.createIfNotExists(DomainExceptions);

        SearchSettingsEntity TopLevelDomainsExceptions = new SearchSettingsEntity();
        TopLevelDomainsExceptions.setSettingName(PropertyKeys.TopLevelDomainsExceptions);
        searchSettingsDao.createIfNotExists(TopLevelDomainsExceptions);

        SearchSettingsEntity KeywordsInSearchResults = new SearchSettingsEntity();
        KeywordsInSearchResults.setSettingName(PropertyKeys.KeywordsInSearchResults);
        searchSettingsDao.createIfNotExists(KeywordsInSearchResults);

        SearchSettingsEntity URLExceptions = new SearchSettingsEntity();
        URLExceptions.setSettingName(PropertyKeys.URLExceptions);
        searchSettingsDao.createIfNotExists(URLExceptions);
    }

    private void initFillApplicationSettingsTable() throws SQLException {
        AppSettingsDao appSettingsDao = DaoManager.createDao(connectionSource, ApplicationSettingsEntity.class);

        ApplicationSettingsEntity workStatus = new ApplicationSettingsEntity();
        workStatus.setSettingName(PropertyKeys.WorkStatus);
        appSettingsDao.createIfNotExists(workStatus);

        ApplicationSettingsEntity dataFilePath = new ApplicationSettingsEntity();
        dataFilePath.setSettingName(PropertyKeys.DataFilePath);
        appSettingsDao.createIfNotExists(dataFilePath);

        ApplicationSettingsEntity searchPlaceholder = new ApplicationSettingsEntity();
        searchPlaceholder.setSettingName(PropertyKeys.SearchPlaceholder);
        appSettingsDao.createIfNotExists(searchPlaceholder);
    }

    private void updatePropertyByKey(PropertyKeys propertyKey, String value) {
        if (value == null || propertyKey == null) {
            return;
        }

        try {
            SearchSettingsDao searchSettingsDao = DaoManager.createDao(connectionSource, SearchSettingsEntity.class);
            SearchSettingsEntity settings = getSearchSettingsPropertyByKey(propertyKey);
            settings.setSettingName(propertyKey);
            settings.setSettingValue(value);

            searchSettingsDao.update(settings);
        } catch (SQLException e) {
            Logger.tag("SYSTEM").error(e, "Cannot update property: " + propertyKey);
        }
    }

    private void updateApplicationSettingsPropertyByKey(PropertyKeys propertyKey, String value) {
        if (value == null || propertyKey == null) {
            return;
        }

        try {
            AppSettingsDao searchSettingsDao = DaoManager.createDao(connectionSource, ApplicationSettingsEntity.class);
            ApplicationSettingsEntity settings = getApplicationSettingsPropertyByKey(propertyKey);
            settings.setSettingName(propertyKey);
            settings.setSettingValue(value);

            searchSettingsDao.update(settings);
        } catch (SQLException e) {
            Logger.tag("SYSTEM").error(e, "Cannot update property: " + propertyKey);
        }
    }

    private SearchSettingsEntity getSearchSettingsPropertyByKey(PropertyKeys propertyKey) {
        if (propertyKey == null) {
            return null;
        }
        SearchSettingsEntity settings = null;
        try {
            SearchSettingsDao searchSettingsDao = DaoManager.createDao(connectionSource, SearchSettingsEntity.class);
            settings = searchSettingsDao.findByKey(propertyKey).get(0);
            if (StringUtils.isEmpty(settings.getSettingValue())) {
                settings.setSettingValue("");
            }
        } catch (SQLException e) {
            Logger.tag("SYSTEM").error(e, "Cannot update property: " + propertyKey);
        }
        return settings;
    }

    private ApplicationSettingsEntity getApplicationSettingsPropertyByKey(PropertyKeys propertyKey) {
        if (propertyKey == null) {
            return null;
        }
        ApplicationSettingsEntity settings = null;
        try {
            AppSettingsDao appSettingsDao = DaoManager.createDao(connectionSource, ApplicationSettingsEntity.class);
            settings = appSettingsDao.findByKey(propertyKey).get(0);
            if (StringUtils.isEmpty(settings.getSettingValue())) {
                settings.setSettingValue("");
            }
        } catch (SQLException e) {
            Logger.tag("SYSTEM").error(e, "Cannot update property: " + propertyKey);
        }
        return settings;
    }

    public void saveSearchSettings(SearchSettings searchSettings) {
        try {
           updatePropertyByKey(PropertyKeys.DomainExceptions, String.join(delimiter, searchSettings.ExceptionsForFoundDomains));
           updatePropertyByKey(PropertyKeys.KeywordsInSearchResults, String.join(delimiter, searchSettings.KeywordsForLookingInSearchResults));
           updatePropertyByKey(PropertyKeys.MetaTagsExceptions, String.join(delimiter, searchSettings.MetaTagsExceptions));
           updatePropertyByKey(PropertyKeys.SpecificWordsInDomainURLs, String.join(delimiter, searchSettings.KeywordsForLookingInDomainURLs));
           updatePropertyByKey(PropertyKeys.TopLevelDomainsExceptions, String.join(delimiter, searchSettings.ExceptionsForTopLevelDomains));
           updatePropertyByKey(PropertyKeys.URLExceptions, String.join(delimiter, searchSettings.ExceptionsForWordsInDomainURLs));
        }
        catch(NullPointerException ex) {
            Logger.tag("SYSTEM").error(ex);
        }
    }

    public SearchSettings getSearchSettings() {
        SearchSettings searchSettings = new SearchSettings();
        try {
            Collections.addAll(searchSettings.ExceptionsForFoundDomains, getSearchSettingsPropertyByKey(PropertyKeys.DomainExceptions).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.KeywordsForLookingInSearchResults, getSearchSettingsPropertyByKey(PropertyKeys.KeywordsInSearchResults).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.MetaTagsExceptions, getSearchSettingsPropertyByKey(PropertyKeys.MetaTagsExceptions).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.KeywordsForLookingInDomainURLs, getSearchSettingsPropertyByKey(PropertyKeys.SpecificWordsInDomainURLs).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.ExceptionsForTopLevelDomains, getSearchSettingsPropertyByKey(PropertyKeys.TopLevelDomainsExceptions).getSettingValue().split(delimiter));
            Collections.addAll(searchSettings.ExceptionsForWordsInDomainURLs, getSearchSettingsPropertyByKey(PropertyKeys.URLExceptions).getSettingValue().split(delimiter));
        } catch (NullPointerException ex) {
            Logger.error(ex);
        }

        return searchSettings;
    }


    public boolean getWorkStatus() {
        return Boolean.valueOf(getApplicationSettingsPropertyByKey(PropertyKeys.WorkStatus).getSettingValue());
    }

    public synchronized void updateWorkStatus(boolean status) {
        updateApplicationSettingsPropertyByKey(PropertyKeys.WorkStatus, String.valueOf(status));
    }


    public String getSearchPlaceholder() {
        return getApplicationSettingsPropertyByKey(PropertyKeys.SearchPlaceholder).getSettingValue();
    }

    public synchronized void updateSearchPlaceholder(String searchPlaceholder) {
        updateApplicationSettingsPropertyByKey(PropertyKeys.SearchPlaceholder, searchPlaceholder);
    }


    public String getDataFilePath() {
        return getApplicationSettingsPropertyByKey(PropertyKeys.DataFilePath).getSettingValue();
    }

    public void updateFileDataPath(String filePath) {
        updateApplicationSettingsPropertyByKey(PropertyKeys.DataFilePath, filePath);
    }
}
