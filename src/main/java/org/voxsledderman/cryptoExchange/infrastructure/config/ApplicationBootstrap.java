package org.voxsledderman.cryptoExchange.infrastructure.config;

import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.database.MySqlConnectionFactory;
import org.voxsledderman.cryptoExchange.infrastructure.database.SQLiteConnectionFactory;
import com.j256.ormlite.table.TableUtils;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.TradeOrderDao;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.WalletDao;

import java.io.File;
import java.sql.SQLException;

@Slf4j
public class ApplicationBootstrap {

    private final AppConfigManager config;
    private final File dataFolder;

    public ApplicationBootstrap(AppConfigManager config, File dataFolder) {
        this.config = config;
        this.dataFolder = dataFolder;
    }

    public ConnectionSource connectToDB(){
        ConnectionSource connectionSource = null;
        if(config.isMySqlEnabled()) {
            try {
                final MySqlConnectionFactory factory = new MySqlConnectionFactory(config.getMySqlConnectionData());
                connectionSource =  factory.getConnectionSource();
            } catch (SQLException e) {
                log.error("MySQL connection failed —> falling back to SQLite", e);
                log.info("Connecting to SQLite database instead");
               connectionSource =  connectToSqlite();
            }
        } else connectionSource =  connectToSqlite();

        initTables(connectionSource);
        return connectionSource;
    }

    private ConnectionSource connectToSqlite() {
        if(!dataFolder.exists()) dataFolder.mkdirs();
        File dbFile = new File(dataFolder, "crypto_exchange.db");

        try {
            final SQLiteConnectionFactory factory = new SQLiteConnectionFactory(dbFile.getAbsolutePath());
            return factory.getConnectionSource();
        } catch (SQLException e) {
            log.error("Unable to connect to any database!");
            e.printStackTrace();
            throw new InternalError();
        }
    }
    private void initTables(ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, WalletDao.class);
            TableUtils.createTableIfNotExists(connectionSource, TradeOrderDao.class);
        } catch (SQLException e) {
            log.error("Failed to create tables!", e);
            throw new InternalError();
        }
    }
}
