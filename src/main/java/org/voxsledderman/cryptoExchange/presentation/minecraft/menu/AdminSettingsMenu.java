package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;

public class AdminSettingsMenu extends Menu{

    public AdminSettingsMenu(MenuType menuType, MenuFactory menuFactory) {
        super(menuType, menuFactory);
    }

    @Override
    public Gui setupGui() {
        return null;
    }

    @Override
    public void playOpenSound() {

    }
}
