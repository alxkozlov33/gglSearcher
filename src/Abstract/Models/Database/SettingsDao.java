package Abstract.Models.Database;

import Abstract.Models.Database.Interfaces.ISettingsDao;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class SettingsDao extends BaseDaoImpl<Settings, Long> implements ISettingsDao {

    protected SettingsDao(Class<Settings> dataClass) throws SQLException {
        super(dataClass);
    }

    public SettingsDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Settings.class);
    }

    @Override
    public List<Settings> findByKey(PropertyKeys propertyKey) throws SQLException {
        return super.queryForEq("name", propertyKey.name());
    }

}
