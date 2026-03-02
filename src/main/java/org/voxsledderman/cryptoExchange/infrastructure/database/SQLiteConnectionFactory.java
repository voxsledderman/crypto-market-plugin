//package org.voxsledderman.cryptoExchange.infrastructure.database;
//import com.j256.ormlite.jdbc.DataSourceConnectionSource;
//import com.j256.ormlite.support.ConnectionSource;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.Getter;
//
//import java.sql.SQLException;
//
//@Getter
//public class SQLiteConnectionFactory {
//    private final String path;
//    private final HikariDataSource dataSource;
//    private final ConnectionSource connectionSource;
//
//    public SQLiteConnectionFactory(String path) throws SQLException {
//        this.path = path;
//
//        HikariConfig config = new HikariConfig();
//        String url = "jdbc:sqlite:%s".formatted(path);
//        config.setJdbcUrl(url);
//        config.setMaximumPoolSize(1);
//        config.setPoolName("SQLitePool");
//
//        this.dataSource = new HikariDataSource(config);
//        this.connectionSource = new DataSourceConnectionSource(dataSource, url);
//    }
//
//    public void close() {
//        if (dataSource != null) {
//            dataSource.close();
//        }
//    }
//}

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
