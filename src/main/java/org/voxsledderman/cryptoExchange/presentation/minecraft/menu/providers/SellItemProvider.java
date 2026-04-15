package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import org.bukkit.Material;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.math.BigDecimal;

public class SellItemProvider {
    public static ItemProvider createProvider(String cryptoName, BigDecimal pickedAmount, BigDecimal price){
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setDisplayName("<red>Sell");
        builder.addLoreLines(
                "<gray>Crypto<dark_gray>:<#F0E6A1> %s".formatted(cryptoName),
                "<gray>Amount<dark_gray>:<red> %s".formatted(getPickedAmount(pickedAmount)),
                "<gray>Price<dark_gray>: <gold>%s <gray>(might change)".formatted(PriceFormatter.formatMoney(price.multiply(pickedAmount)))
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
