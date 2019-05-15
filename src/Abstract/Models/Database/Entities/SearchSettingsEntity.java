package Abstract.Models.Database.Entities;

import Abstract.Models.Database.DAO.SearchSettingsDao;
import Utils.PropertyKeys;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "SearchSettings", daoClass = SearchSettingsDao.class)
public class SearchSettingsEntity {

    @DatabaseField(generatedId = true)
    private long settingId;

    @DatabaseField(canBeNull = false)
    private String settingName;

    @DatabaseField
    private String settingValue;

    public SearchSettingsEntity() {

    }

    public long getSettingId() {
        return settingId;
    }

    public void setSettingId(long settingId) {
        this.settingId = settingId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public void setSettingName(PropertyKeys propertyKey) {
        this.settingName = propertyKey.name();
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
}
