package Abstract.Models.Database.Interfaces;

import Abstract.Models.Database.Entities.ApplicationSettingsEntity;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;

public interface IAppSettingsDao extends Dao<ApplicationSettingsEntity, Long> {
    List<ApplicationSettingsEntity> findByKey(PropertyKeys key) throws SQLException;
}