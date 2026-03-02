package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class BackItem extends PageItem {
    public BackItem(boolean forward) {
        super(forward);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.ARROW);
        if(getGui().hasPreviousPage()){
            builder.setDisplayName("Go to previous page");
        } else {
            builder.setDisplayName("You're on the first page");
        }
        builder.addLoreLines(
                "Page (%d/%d)".formatted(gui.getCurrentPage() + 1, gui.getPageAmount())
        );
        return builder;
    }
}
