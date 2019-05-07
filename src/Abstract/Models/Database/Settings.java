package Abstract.Models.Database;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "settings")
public class Settings {

    @DatabaseField(generatedId = true)
    private long settingId;

    @DatabaseField(canBeNull = false)
    private String settingName;

    @DatabaseField
    private String settingValue;
}
