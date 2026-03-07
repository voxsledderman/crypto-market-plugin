
package org.voxsledderman.cryptoExchange.infrastructure.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;

import java.sql.SQLException;

@Getter
public class SQLiteConnectionFactory {
    private final String path;
    private final ConnectionSource connectionSource;

    public SQLiteConnectionFactory(String path) throws SQLException {
        this.path = path;
        String url = "jdbc:sqlite:" + path;

        this.connectionSource = new JdbcConnectionSource(url);
    }

    public void close() {
        try {
            connectionSource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
