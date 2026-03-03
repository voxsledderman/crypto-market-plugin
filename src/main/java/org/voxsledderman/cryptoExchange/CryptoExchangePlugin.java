package org.voxsledderman.cryptoExchange;

import com.j256.ormlite.support.ConnectionSource;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.message.LiteMessages;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.impl.economyRepo.VaultEconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.impl.walletRepo.CachedWalletRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.impl.walletRepo.OrmLiteWalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.ApplicationBootstrap;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.providers.BinanceWebSocketProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.command.ExchangeCommand;

import java.sql.SQLException;

@Getter
public final class CryptoExchangePlugin extends JavaPlugin {

    private final AppConfigManager appConfigManager = new AppConfigManager(this);
    private final MenuConfigManager menuConfigManager = new MenuConfigManager(this, false);
    private BinanceWebSocketProvider binanceWebSocketProvider;
    private ConnectionSource connectionSource;
    private WalletRepository walletRepository;
    private EconomyRepository economyRepository;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        ApplicationBootstrap appBootstrap = new ApplicationBootstrap(appConfigManager, getDataFolder());
        connectionSource = appBootstrap.connectToDB();

        try {
            WalletRepository ormRepo = new OrmLiteWalletRepository(connectionSource);
            this.walletRepository = new CachedWalletRepository(ormRepo);

        } catch (SQLException e) {
            getLogger().severe("Error while enabling repository: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Economy economy = setupEconomy();
        if(economy == null) {
            getLogger().severe("Unable to load Vault Economy, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        economyRepository = new VaultEconomyRepository(economy);

        Bukkit.getScheduler().runTaskLater(this, () ->{
            binanceWebSocketProvider = new BinanceWebSocketProvider(appConfigManager.getTrackedTickers(), appConfigManager.getQuoteCurrency());
            this.liteCommands = LiteBukkitFactory.builder("voxsledderman", this)
                    .commands(
                            new ExchangeCommand(new MenuContext(appConfigManager, menuConfigManager, walletRepository, economyRepository), binanceWebSocketProvider, this)
                    )
                    .message(LiteMessages.MISSING_PERMISSIONS, permission -> "§cNie masz permisji na wykonanie tej komendy!")
                    .message(LiteMessages.INVALID_USAGE, invalidUsage ->  "§cNiepoprawne użycie komendy!")
                    .build();
        }, 40);
    }


    @Override
    public void onDisable() {
        try {
            if(connectionSource != null) {
                connectionSource.close();
            }
        } catch (Exception e) {
            getLogger().severe("Failed to close database connection");
            throw new RuntimeException(e);
        }

        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }
        binanceWebSocketProvider.shutdown();
    }

    private Economy setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }
}
