package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.CryptoExchangePlugin;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.BuySellCryptoMenu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PickAmountItem extends AbstractItem {
    private final BigDecimal pickedAmount;
    private final JavaPlugin plugin;
    private final MenuContext menuContext;
    private final CryptoItem cryptoItem;
    private final String ticker;

    public PickAmountItem(BigDecimal pickedAmount, JavaPlugin plugin, MenuContext menuContext, CryptoItem cryptoItem, String ticker) {
        this.pickedAmount = pickedAmount;
        this.plugin = plugin;
        this.menuContext = menuContext;
        this.cryptoItem = cryptoItem;
        this.ticker = ticker;
    }

    @Override
    public ItemProvider getItemProvider(){
        return new ItemBuilder(Material.HOPPER)
                .setDisplayName("Pick crypto amount to buy/sell")
                .addLoreLines(
                        " ",
                        "picked amount -> %s".formatted(pickedAmount)
                        );
    }

    @Override
    //TODO: Refactor
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
       var exchange = (CryptoExchangePlugin) plugin;
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Enter amount")
                .text(pickedAmount.toPlainString())
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String potentialNum = stateSnapshot.getText().replace(",", ".");

                    if (isBigDecimal(potentialNum)) {
                        BigDecimal newAmount = new BigDecimal(potentialNum);

                        if(newAmount.compareTo(BigDecimal.ZERO) <= 0) {
                            return List.of(AnvilGUI.ResponseAction.replaceInputText("Number must be greater than 0!"));
                        }
                        player.sendMessage("§aSet amount to: " + newAmount.toPlainString());

                        List<AnvilGUI.ResponseAction> actions = new ArrayList<>();
                        actions.add(AnvilGUI.ResponseAction.close());

                        Menu menu = new BuySellCryptoMenu(menuContext, MenuType.BUY_OR_SELL_CRYPTO, cryptoItem, newAmount, plugin,
                                exchange.getBinanceWebSocketProvider(), menuContext.getWalletRepository().findByUuid(player.getUniqueId()).orElse(null), ticker);
                        menu.openMenu(player);
                        return actions;

                    } else {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("Invalid number!"));
                    }
                })
                .open(player);
    }
    private boolean isBigDecimal(String string){
        if(string == null) return false;
        try{
            new BigDecimal(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
