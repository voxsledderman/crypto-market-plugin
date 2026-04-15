package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class TurnBackItem extends AbstractItem {

    private final Menu backMenu;

    public TurnBackItem(Menu backMenu) {
        this.backMenu = backMenu;
    }

    @Override
    public ItemProvider getItemProvider(){
        return new ItemBuilder(Material.ENDER_PEARL).setDisplayName("<yellow>Open previous menu");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        backMenu.openMenu(player);
    }
}
