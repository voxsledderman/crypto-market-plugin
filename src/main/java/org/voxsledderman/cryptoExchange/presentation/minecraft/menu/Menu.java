package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

@Getter
public abstract class Menu  {
   private Window window;
   private final JavaPlugin plugin;
   private final MenuType menuType;
   private final MenuContext menuContext;
    public Menu(JavaPlugin plugin, MenuContext menuContext, MenuType menuType) {
        this.plugin = plugin;
        this.menuType = menuType;
        this.menuContext = menuContext;

    }

    public Component setupTitle(){
        return getMenuConfigManager().getTitleByType(menuType);
    };

   public abstract Gui setupGui();

   public abstract void playOpenSound();

    public void openMenu(Player player) {
        window = Window.single().setViewer(player).setGui(setupGui()).setTitle(
                new AdventureComponentWrapper(setupTitle())).build();
        window.open();
        playOpenSound();
    }

    protected AppConfigManager getAppConfigManager(){
        return menuContext.getAppConfigManager();
    }

    protected MenuConfigManager getMenuConfigManager(){
        return menuContext.getMenuConfigManager();
    }

    protected EconomyRepository getEconomyRepository(){
        return menuContext.getEconomyRepository();
    }

    protected WalletRepository getWalletRepository(){
        return menuContext.getWalletRepository();
    }


}
