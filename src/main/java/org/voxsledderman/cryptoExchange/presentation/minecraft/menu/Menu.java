package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

@Getter
public abstract class Menu  {
   private final AppConfigManager appConfigManager;
   private final MenuConfigManager menuConfigManager;
   private Window window;
   private final MenuType menuType;
   private final WalletRepository walletRepository;
   private final EconomyRepository economyRepository;

    public Menu(AppConfigManager appConfigManager, MenuType menuType, MenuConfigManager menuConfigManager, WalletRepository walletRepository, EconomyRepository economyRepository) {
        this.appConfigManager = appConfigManager;
        this.menuConfigManager = menuConfigManager;
        this.menuType = menuType;
        this.walletRepository = walletRepository;
        this.economyRepository = economyRepository;
    }

    public Component setupTitle(){
        return menuConfigManager.getTitleByType(menuType);
    };

   public abstract Gui setupGui();

   public abstract void playOpenSound();

    public void openMenu(Player player) {
        window = Window.single().setViewer(player).setGui(setupGui()).setTitle(
                new AdventureComponentWrapper(setupTitle())).build();
        window.open();
        playOpenSound();
    }


}
