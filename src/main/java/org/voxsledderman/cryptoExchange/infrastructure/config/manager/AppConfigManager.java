package org.voxsledderman.cryptoExchange.infrastructure.config.manager;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.voxsledderman.cryptoExchange.CryptoExchangePlugin;
import org.voxsledderman.cryptoExchange.domain.market.QuoteCurrency;
import org.voxsledderman.cryptoExchange.domain.validators.MarketConfigValidator;
import org.voxsledderman.cryptoExchange.domain.validators.MySqlValidator;
import org.voxsledderman.cryptoExchange.infrastructure.config.MySqlConnectionData;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class AppConfigManager {

    private final CryptoExchangePlugin plugin;
    private QuoteCurrency quoteCurrency;
    private List<String> trackedTickers;
    private BigDecimal spread;
    private boolean mySqlEnabled;
    private MySqlConnectionData mySqlConnectionData;



    public AppConfigManager(CryptoExchangePlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        String currency = config.getString("market.quote-currency", "USDT").toUpperCase();
        List<String> tickers = config.getStringList("market.tracked-cryptos");
        BigDecimal spread = BigDecimal.valueOf(config.getDouble("market.spread",0.5));

        this.quoteCurrency = MarketConfigValidator.validateCurrency(currency);
        this.trackedTickers = List.copyOf(MarketConfigValidator.validateTrackedTickers(tickers, quoteCurrency));
        this.spread = MarketConfigValidator.validateSpread(spread);

        String mySqlPath = "MySQL_database";
        mySqlEnabled = config.getBoolean(mySqlPath + ".enabled", false);

        String database = config.getString(mySqlPath + ".database");
        String host = config.getString(mySqlPath + ".host");
        int port = config.getInt(mySqlPath +".port", 3306);
        String user = config.getString(mySqlPath + ".user");
        String password = config.getString(mySqlPath + ".password");
        int maxConnections = config.getInt(mySqlPath + ".maximum_connections_hikariCP", 25);

        if(mySqlEnabled){
            mySqlConnectionData = new MySqlConnectionData(
                    MySqlValidator.validateHost(host), port, MySqlValidator.validateDatabaseName(database), MySqlValidator.validateUser(user),
                    password, maxConnections
            );
        }

    }
}