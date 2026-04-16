package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.Bukkit;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.*;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;

import java.util.UUID;

public class AdminPanelMenu extends Menu{

    private final Menu turnBackMenu;
    private final MenuContext menuContext;
    public AdminPanelMenu(MenuType menuType, MenuFactory menuFactory, UUID adminUUID, MenuContext menuContext) {
        super(menuType, menuFactory);
        this.menuContext = menuContext;
        this.turnBackMenu = new MainMenu(Bukkit.getPlayer(adminUUID), menuFactory);
    }

    @Override
    public Gui setupGui() {
        return Gui.normal().setStructure(
                "A . . . . . . . X",
                ". . . . . . . . .",
                ". . H . C . S . .",
                ". . . . . . . . .",
                "P . . . . . . . ."

        )

                .addIngredient('P', new TurnBackItem(turnBackMenu))
                .addIngredient('X', new CloseItem())
                .addIngredient('H', new TransactionHistoryItem())
                .addIngredient('C', new ReloadConfigItem(menuContext, getPlugin()))
                .addIngredient('S', new SettingsItem())

                .build();
    }

    @Override
    public void playOpenSound() {

    }
}
