package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class PageItemProvider {

    public static ItemProvider createBackItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.ARROW);
        if(gui.hasPreviousPage()){
            builder.setDisplayName("<green>Go to previous page");
        } else {
            builder.setDisplayName("<#F0E6A1>You're on the first page");
        }
        builder.addLoreLines(
                "Page (%d/%d)".formatted(gui.getCurrentPage() + 1, gui.getPageAmount())
        );
        return builder;
    }
    public static ItemProvider createNextItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.ARROW);
        if(gui.hasNextPage()){
            builder.setDisplayName("<green>Go to next page");
        } else {
            builder.setDisplayName("<#F0E6A1>You're on the last page");
        }
        builder.addLoreLines(
                "Page (%d/%d)".formatted(gui.getCurrentPage() + 1, gui.getPageAmount())
        );
        return builder;
    }
}
