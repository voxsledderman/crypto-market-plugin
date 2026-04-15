package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class WalletItemProvider {

    public static ItemProvider createProvider(PriceProvider priceProvider, MenuContext menuContext, Wallet wallet){
        var tickers = priceProvider.getFullMarketData(menuContext.getAppConfigManager().getTrackedTickers());
        String currentValue = PriceFormatter.formatMoney(
                WalletCalculator.getCurrentPortfolioValue(wallet, tickers));
        String roiSuffix = Utility.getChangePercentSuffix(WalletCalculator.getCurrentROI(wallet, tickers).toString());

        ItemBuilder builder = new ItemBuilder(Material.BOOK);
        builder.setDisplayName(new AdventureComponentWrapper(
                Component.text("<white><bold>Your portfolio")
        ));
        builder.addLoreLines(" ", "<gray>Portfolio value<dark_gray>: <gold>%s <white>| %s".formatted(currentValue, roiSuffix));

        return builder;
    }
}
