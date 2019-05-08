package Abstract.Models.Database.Interfaces;

import Abstract.Models.Database.Settings;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;

public interface ISettingsDao extends Dao<Settings, Long> {
    public List<Settings> findByKey(PropertyKeys key) throws SQLException;
}