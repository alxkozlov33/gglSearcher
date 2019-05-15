package Abstract.Models.Database.DAO;

import Abstract.Models.Database.Entities.SearchSettingsEntity;
import Abstract.Models.Database.Interfaces.ISettingsDao;
import Utils.PropertyKeys;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class SearchSettingsDao extends BaseDaoImpl<SearchSettingsEntity, Long> implements ISettingsDao {

    protected SearchSettingsDao(Class<SearchSettingsEntity> dataClass) throws SQLException {
        super(dataClass);
    }

    public SearchSettingsDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SearchSettingsEntity.class);
    }

    @Override
    public List<SearchSettingsEntity> findByKey(PropertyKeys propertyKey) throws SQLException {
        return super.queryForEq("settingName", propertyKey.name());
    }

}
