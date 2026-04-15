package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.math.BigDecimal;

public class PickAmountItem extends AbstractItem {
    private final BigDecimal pickedAmount;
    private final CryptoItem cryptoItem;
    private final Menu turnBackMenu;
    private final MenuFactory menuFactory;

    public PickAmountItem(BigDecimal pickedAmount, CryptoItem cryptoItem, Menu turnBackMenu, MenuFactory menuFactory) {
        this.pickedAmount = pickedAmount;
        this.cryptoItem = cryptoItem;
        this.turnBackMenu = turnBackMenu;
        this.menuFactory = menuFactory;
    }

    @Override
    public ItemProvider getItemProvider(){
        return new ItemBuilder(Material.HOPPER)
                .setDisplayName("<bold>Click to pick amount")
                .addLoreLines(
                        " ",
                        "<gray>picked amount<dark_gray> >> <yellow>%s".formatted(pickedAmount)
                        );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        menuFactory.createAnvilAmountPickerMenu().openAnvilInputMenu(player, cryptoItem, turnBackMenu, pickedAmount);
    }
}
