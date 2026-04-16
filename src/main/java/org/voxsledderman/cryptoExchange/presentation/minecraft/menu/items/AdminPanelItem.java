package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.AdminPanelItemProvider;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class AdminPanelItem extends AbstractItem{

    private final MenuFactory menuFactory;
    private final String viewPermission;

    public AdminPanelItem(MenuFactory menuFactory, String viewPermission) {
        this.menuFactory = menuFactory;
        this.viewPermission = viewPermission;
    }

    @Override
    public ItemProvider getItemProvider(){
        return AdminPanelItemProvider.createProvider();
    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if(player.hasPermission(viewPermission)){
            Menu menu = menuFactory.createAdminPanelMenu(player.getUniqueId());
            menu.openMenu(player);
        } else {
            player.sendMessage("<red>You don't have permission to open this menu!");
        }
    }
}
