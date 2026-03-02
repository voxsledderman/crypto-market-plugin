package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.application.usecases.GetOrCreateWalletUseCase;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.PortfolioMenu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.UUID;

public class WalletItem extends AbstractItem {
   private final Wallet wallet;
   private final PriceProvider priceProvider;
   private final MenuContext menuContext;

    public WalletItem(UUID uuid , PriceProvider priceProvider, MenuContext menuContext, GetOrCreateWalletUseCase getOrCreateWalletUseCase) {
        this.priceProvider = priceProvider;
        this.menuContext = menuContext;
        wallet = getOrCreateWalletUseCase.getOrCreateWallet(uuid);
    }

    @Override
    public ItemProvider getItemProvider(){
        var tickers = priceProvider.getFullMarketData(menuContext.getAppConfigManager().getTrackedTickers());

        String currentValue = PriceFormatter.formatMoney(
                WalletCalculator.getPortfolioValue(wallet, tickers));
        String roi = PriceFormatter.formatPercentage(WalletCalculator.getROI(wallet, tickers).toString());

        ItemBuilder builder = new ItemBuilder(Material.BOOK);
        builder.setDisplayName("Your portfolio");
        builder.addLoreLines(" ", "Portfolio value: %s (%s)".formatted(currentValue, roi));

        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if(wallet.getOrders().isEmpty()){
            return;
        }
        Menu menu = new PortfolioMenu(MenuType.OPENED_POSITIONS, wallet,
                priceProvider, PositionState.OPENED, menuContext);
        menu.openMenu(player);
    }
}
