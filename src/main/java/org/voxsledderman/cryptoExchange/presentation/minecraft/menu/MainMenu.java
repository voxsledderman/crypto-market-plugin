package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.*;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu extends Menu{
    private final Player player;
    private final PriceProvider priceProvider;
    public MainMenu(AppConfigManager appConfigManager, MenuConfigManager menuConfigManager,
                    Player player, PriceProvider priceProvider, WalletRepository walletRepository, EconomyRepository economyRepository) {
        super(appConfigManager, MenuType.MAIN, menuConfigManager, walletRepository, economyRepository);
        this.player = player;
        this.priceProvider = priceProvider;
    }

    @Override
    public Gui setupGui() {
        List<Item> cryptoItems = new ArrayList<>();
        Map<String, CryptoInfo> map = priceProvider.getFullMarketData(getAppConfigManager().getTrackedTickers());

        for(String key : map.keySet()){
            cryptoItems.add(new CryptoItem(new CryptoInfo(map.get(key).fullName(), map.get(key).price(), map.get(key).changePercent())));
        }
        return PagedGui.items()
                .setStructure(
                        "P . . . . . . . ." ,
                        ". . C C C C C . ." ,
                        "B . C C C C C . N" ,
                        ". . C C C C C . ." ,
                        "E . . . . . . . ."

                )
                .addIngredient('C', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('E', new CloseItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("Close menu")))
                .addIngredient('B', new BackItem(false))
                .addIngredient('N', new NextItem(true))
                .addIngredient('P', new WalletItem(player.getUniqueId(), getWalletRepository(), priceProvider,
                        getAppConfigManager(), getMenuConfigManager(), getEconomyRepository()))
                .setContent(cryptoItems)
                .build();
    }

    @Override
    public void playOpenSound() {

    }
}
