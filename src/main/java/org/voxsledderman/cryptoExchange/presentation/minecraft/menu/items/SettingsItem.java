package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.SettingsItemProvider;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class SettingsItem extends AbstractItem{

    @Override
    public ItemProvider getItemProvider(){
        return SettingsItemProvider.createProvider();
    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {

    }
}
