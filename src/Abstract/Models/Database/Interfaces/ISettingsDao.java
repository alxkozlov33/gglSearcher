package Abstract.Models.Database.Interfaces;

import Abstract.Models.Database.Entities.SearchSettingsEntity;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;

public interface ISettingsDao extends Dao<SearchSettingsEntity, Long> {
    List<SearchSettingsEntity> findByKey(PropertyKeys key) throws SQLException;
}