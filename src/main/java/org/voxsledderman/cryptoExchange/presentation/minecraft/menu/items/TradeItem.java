package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.TradeItemProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;

import java.math.BigDecimal;

public class TradeItem extends AutoUpdateItem {
    private final Wallet wallet;
    private final String ticker;
    private final PositionState positionState;
    private final MenuFactory menuFactory;

    public TradeItem(Wallet wallet, String ticker, PositionState positionState, MenuFactory menuFactory) {
        super(3 * 20, () -> {
            CryptoInfo cryptoInfo = menuFactory.getPriceProvider().getCurrentData(ticker);
            return TradeItemProvider.createProvider(wallet, ticker, cryptoInfo, positionState);
        });
        this.wallet = wallet;
        this.ticker = ticker;
        this.positionState = positionState;
        this.menuFactory = menuFactory;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        CryptoItem cryptoItem = new CryptoItem(ticker, menuFactory);
        MenuType menuType = positionState == PositionState.OPENED
                ? MenuType.OPENED_POSITIONS : MenuType.CLOSED_POSITIONS;

        Menu backMenu = menuFactory.createPortfolioMenu(menuType, wallet, positionState);
        Menu buySellMenu = menuFactory.createBuySellCryptoMenu(cryptoItem, BigDecimal.ZERO, wallet, backMenu);

        buySellMenu.openMenu(player);
    }
}
