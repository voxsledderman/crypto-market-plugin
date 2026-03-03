package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.CryptoExchangePlugin;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.BuySellCryptoMenu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CryptoItem extends AutoUpdateItem {
    private final MenuContext menuContext;
    private final JavaPlugin plugin;
    private final String ticker;


    public CryptoItem(String ticker, PriceProvider priceProvider, MenuContext menuContext, JavaPlugin plugin, String ticker1) {
        super(20 * 2, () -> {
            CryptoInfo cryptoInfo = priceProvider.getCurrentData(ticker);
           return createProvider(cryptoInfo);
        });
        this.menuContext = menuContext;
        this.plugin = plugin;
        this.ticker = ticker1;
    }


    private static ItemProvider createProvider(CryptoInfo info) {
        return new ItemBuilder(Material.GOLD_NUGGET)
                .setDisplayName(info.fullName())
                .setLore(List.of(
                        new AdventureComponentWrapper(
                                Component.text("Price: %s".formatted(PriceFormatter.formatMoney(info.price())))
                        ),
                        new AdventureComponentWrapper(
                                Component.text("24h change: %s".formatted(PriceFormatter.formatPercentage(info.changePercent())))
                        )
                ));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        var exchange = (CryptoExchangePlugin) plugin; //TODO: xd
        Menu menu = new BuySellCryptoMenu(menuContext, MenuType.BUY_OR_SELL_CRYPTO, this, BigDecimal.ZERO, plugin,
                exchange.getBinanceWebSocketProvider(), menuContext.getWalletRepository().findByUuid(player.getUniqueId()).orElse(null), ticker);
        menu.openMenu(player);
    }
}
