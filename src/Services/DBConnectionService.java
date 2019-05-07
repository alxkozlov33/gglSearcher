package Services;

import Abstract.Models.Database.Settings;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.tinylog.Logger;

import java.sql.SQLException;

public class DBConnectionService {

    static JdbcPooledConnectionSource connectionSource;

    public DBConnectionService() {
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:h2:mem:gglSearcherDb");
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    public void createTableIfNotExists() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Settings.class);
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

}
