package Abstract.Models.Database.DAO;

import Abstract.Models.Database.Entities.ApplicationSettingsEntity;
import Abstract.Models.Database.Interfaces.IAppSettingsDao;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class AppSettingsDao extends BaseDaoImpl<ApplicationSettingsEntity, Long> implements IAppSettingsDao {

    protected AppSettingsDao(Class<ApplicationSettingsEntity> dataClass) throws SQLException {
        super(dataClass);
    }

    public AppSettingsDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ApplicationSettingsEntity.class);
    }

    @Override
    public List<ApplicationSettingsEntity> findByKey(PropertyKeys propertyKey) throws SQLException {
        return super.queryForEq("settingName", propertyKey.name());
    }
}
