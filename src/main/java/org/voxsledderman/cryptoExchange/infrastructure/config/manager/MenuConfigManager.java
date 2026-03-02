package org.voxsledderman.cryptoExchange.infrastructure.config.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.voxsledderman.cryptoExchange.CryptoExchangePlugin;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MenuConfigManager {

    private final CryptoExchangePlugin plugin;
    private final Map<MenuType, Component> menuTitleByTypeMap = new HashMap<>();

    public MenuConfigManager(CryptoExchangePlugin plugin, boolean needReloading) {
        this.plugin = plugin;
        loadConfig(needReloading);
    }

    public void loadConfig(boolean needReloading){
        if(needReloading){
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
        }
        FileConfiguration config = plugin.getConfig();
        String defPath = "menu.";

        Arrays.stream(MenuType.values()).forEach(
                menuType -> {
                    Component title = MiniMessage.miniMessage().deserialize(
                            config.getString(defPath + menuType.getSpecificPathToConfig(), menuType.getDefaultTitle()));
                    menuTitleByTypeMap.put(menuType, title);
                }
        );
    }

    public Component getTitleByType(MenuType menuType){
        return menuTitleByTypeMap.get(menuType);
    }
}
