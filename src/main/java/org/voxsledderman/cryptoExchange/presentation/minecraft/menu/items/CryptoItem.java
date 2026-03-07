package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.CryptoItemProvider;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;

import java.math.BigDecimal;

@Getter
public class CryptoItem extends AutoUpdateItem {
    private final String ticker;
    private final MenuFactory menuFactory;

    public CryptoItem(String ticker, MenuFactory menuFactory) {
        super(20 * 2, () -> {
            CryptoInfo cryptoInfo = menuFactory.getPriceProvider().getCurrentData(ticker);
            return CryptoItemProvider.createProvider(cryptoInfo);
        });
        this.ticker = ticker;
        this.menuFactory = menuFactory;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        Wallet wallet =  menuFactory.getMenuContext().getWalletRepository().findByUuid(player.getUniqueId()).orElse(null);
        if(wallet == null) return;
        Menu menu = menuFactory.createBuySellCryptoMenu(this, BigDecimal.ZERO, wallet, menuFactory.createMainMenu(player));

        menu.openMenu(player);
    }
}
