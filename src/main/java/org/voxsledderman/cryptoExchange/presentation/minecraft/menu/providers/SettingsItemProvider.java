package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class SettingsItemProvider {
    public static ItemProvider createProvider(){
        return new ItemBuilder(Material.WRITABLE_BOOK).setDisplayName("Admin settings").addLoreLines(
                "<gray>only permitted players can see this",
                "<yellow>click to open plugin settings");
    }
}
