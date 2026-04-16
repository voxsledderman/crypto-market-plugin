package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;


public class ReloadConfigItem extends AbstractItem {
    private final MenuContext menuContext;
    private final Plugin plugin;

    public ReloadConfigItem(MenuContext menuContext, Plugin plugin) {
        this.menuContext = menuContext;
        this.plugin = plugin;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PAPER).setDisplayName("<green>Reload Config File");
    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
       Bukkit.getScheduler().runTaskAsynchronously(
                plugin, () -> {
                    menuContext.getAppConfigManager().loadConfig();
                    menuContext.getMenuConfigManager().loadConfig(true);
                    player.sendMessage("<green>[CryptoExchanghe] Config reloaded!");
                    notifyWindows();
                }
        );

    }
}
