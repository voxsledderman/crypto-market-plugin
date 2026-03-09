package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.List;

public class CryptoItemProvider{

    public static ItemProvider createProvider(CryptoInfo info) {
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
}
