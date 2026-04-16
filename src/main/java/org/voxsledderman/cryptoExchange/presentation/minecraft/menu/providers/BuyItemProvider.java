package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import org.bukkit.Material;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.math.BigDecimal;

public class BuyItemProvider {

    public static ItemProvider createProvider(String cryptoName, BigDecimal pickedAmount, BigDecimal price, AppConfigManager appConfigManager){
        ItemBuilder builder = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE);
        BigDecimal spread = appConfigManager.getSpread();
        builder.setDisplayName("<green>Buy");
        BigDecimal priceAfterSpread = price.multiply(BigDecimal.ONE.add(spread));
        builder.addLoreLines(
                "<gray>Crypto<dark_gray>:<#F0E6A1> %s".formatted(cryptoName),
                "<gray>Amount<dark_gray>:<green> %s".formatted(getPickedAmount(pickedAmount)),
                "<gray>Price<dark_gray>:<gold> %s <gray>(might change)".formatted(PriceFormatter.formatMoney(priceAfterSpread.multiply(pickedAmount))),
                "<gray>Spread<dark_gray>: <dark_green>%s".formatted(PriceFormatter.formatPercentage(spread.multiply(
                        BigDecimal.valueOf(100)).toString()))
        );
        return builder;
    }
    private static String getPickedAmount(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) == 0){
            return amount + "<gray> - You need to specify amount!";
        }
        else return amount.toString();
    }
}
