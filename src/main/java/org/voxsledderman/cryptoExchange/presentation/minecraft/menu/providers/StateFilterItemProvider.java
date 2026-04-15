package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import org.bukkit.Material;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class StateFilterItemProvider {

    public static ItemProvider createProvider(PositionState positionState){
        ItemBuilder builder;
        if(positionState.equals(PositionState.OPENED)) {
            builder = new ItemBuilder(Material.GLOW_INK_SAC);
            builder.addLoreLines("<green>Opened Positions");
        }
        else{
            builder = new ItemBuilder(Material.INK_SAC);
            builder.addLoreLines("<red>Closed Positions");
        }
        builder.setDisplayName("Position Filter");
        return builder;
    }
}
