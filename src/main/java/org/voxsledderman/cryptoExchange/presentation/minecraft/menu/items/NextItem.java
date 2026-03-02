package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class NextItem extends PageItem {

    public NextItem(boolean forward) {
        super(forward);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.ARROW);
        if(getGui().hasNextPage()){
            builder.setDisplayName("Go to next page");
        } else {
            builder.setDisplayName("You're on the last page");
        }
        builder.addLoreLines(
                "Page (%d/%d)".formatted(gui.getCurrentPage() + 1, gui.getPageAmount())
        );
        return builder;
    }
}
