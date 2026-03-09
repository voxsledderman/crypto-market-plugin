package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

@Getter
public abstract class Menu  {

    public static final String ADMIN_VIEW_SETTINGS_PERM = "cryptoExchange.admin.view.settings";
    public static final String ADMIN_CHANGE_SETTINGS_PERM = "cryptoExchange.admin.change.settings";

   private Window window;
   private final MenuType menuType;
   private final MenuFactory menuFactory;

    public Menu(MenuType menuType, MenuFactory menuFactory) {
        this.menuType = menuType;
        this.menuFactory = menuFactory;

    }

    public Component setupTitle(){
        return getMenuConfigManager().getTitleByType(menuType);
    }

   public abstract Gui setupGui();

   public abstract void playOpenSound();

    public void openMenu(Player player) {
        window = Window.single().setViewer(player).setGui(setupGui()).setTitle(
                new AdventureComponentWrapper(setupTitle())).build();
        window.open();
        playOpenSound();
    }

    protected AppConfigManager getAppConfigManager(){
        return menuFactory.getMenuContext().getAppConfigManager();
    }

    protected MenuConfigManager getMenuConfigManager(){
        return menuFactory.getMenuContext().getMenuConfigManager();
    }

    protected EconomyRepository getEconomyRepository(){
        return menuFactory.getMenuContext().getEconomyRepository();
    }

    protected WalletRepository getWalletRepository(){
        return menuFactory.getMenuContext().getWalletRepository();
    }

    protected JavaPlugin getPlugin(){
        return menuFactory.getPlugin();
    }

}
