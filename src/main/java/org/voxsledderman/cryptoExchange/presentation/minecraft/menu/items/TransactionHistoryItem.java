package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class TransactionHistoryItem extends AbstractItem {

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.OAK_SIGN).setDisplayName("<yellow>Transaction History");
    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        player.sendMessage("<red>Work in progress...");
    }
}
