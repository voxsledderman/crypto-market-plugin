package org.voxsledderman.cryptoExchange.presentation.minecraft.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.MainMenu;

@Command(name = "exchange", aliases = {"ex", "giełda"})
public class ExchangeCommand {
    private final AppConfigManager appConfigManager;
    private final MenuConfigManager menuConfigManager;
    private final PriceProvider priceProvider;
    private final WalletRepository walletRepository;
    private final EconomyRepository economyRepository;

    public ExchangeCommand(AppConfigManager appConfigManager, MenuConfigManager menuConfigManager,
                           PriceProvider priceProvider, WalletRepository walletRepository, EconomyRepository economyRepository) {
        this.appConfigManager = appConfigManager;
        this.menuConfigManager = menuConfigManager;
        this.priceProvider = priceProvider;
        this.walletRepository = walletRepository;
        this.economyRepository = economyRepository;
    }

    @Execute
    void openExchange(@Context CommandSender sender){
        if(sender instanceof Player player){
            MainMenu menu = new MainMenu(appConfigManager, menuConfigManager,player, priceProvider, walletRepository, economyRepository);
            menu.openMenu(player);

        }
    }
}
