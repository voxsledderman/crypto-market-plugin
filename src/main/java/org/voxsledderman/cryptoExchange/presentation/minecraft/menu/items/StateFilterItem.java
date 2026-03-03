package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.PortfolioMenu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Objects;

@Getter
public class StateFilterItem extends AbstractItem {
    private final PositionState positionState;
    private final Wallet wallet;
    private final PriceProvider priceProvider;
    private final MenuContext menuContext;
    private final JavaPlugin plugin;

    public StateFilterItem(PositionState currentState, Wallet wallet, PriceProvider priceProvider, MenuContext menuContext, JavaPlugin plugin) {
        positionState = currentState;
        this.wallet = wallet;
        this.priceProvider = priceProvider;
        this.menuContext = menuContext;
        this.plugin = plugin;
    }


    @Override
    public ItemProvider getItemProvider(){
        ItemBuilder builder;
        if(positionState.equals(PositionState.OPENED)) {
            builder = new ItemBuilder(Material.GLOW_INK_SAC);
            builder.addLoreLines("§aOpened Positions");
        }
        else{
            builder = new ItemBuilder(Material.INK_SAC);
            builder.addLoreLines("§cClosed Positions");
        }

        return builder;
    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        PositionState newPositionState;
        MenuType menuType;

        if (Objects.requireNonNull(positionState) == PositionState.OPENED) {
            newPositionState = PositionState.CLOSED;
            menuType = MenuType.CLOSED_POSITIONS;
        } else {
            newPositionState = PositionState.OPENED;
            menuType = MenuType.OPENED_POSITIONS;
        }
        Menu menu = new PortfolioMenu(menuType, wallet, priceProvider,
                newPositionState, menuContext, plugin);
        menu.openMenu(player);
    }
}
