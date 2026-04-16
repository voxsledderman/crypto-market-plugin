package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.application.usecases.GetOrCreateWalletUseCase;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.*;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu extends Menu{

    private final Player player;
    private final PriceProvider priceProvider;
    private final MenuFactory menuFactory;
    public MainMenu(Player player, MenuFactory menuFactory) {
        super(MenuType.MAIN, menuFactory);
        this.player = player;
        this.priceProvider = menuFactory.getPriceProvider();
        this.menuFactory = menuFactory;
    }

    @Override
    public Gui setupGui() {
        List<Item> cryptoItems = new ArrayList<>();
        Map<String, CryptoInfo> map = priceProvider.getFullMarketData(getAppConfigManager().getTrackedTickers());
        Item settingsItem = player.hasPermission(Menu.ADMIN_VIEW_SETTINGS_PERM) || player.hasPermission(Menu.ADMIN_CHANGE_SETTINGS_PERM)
                ? new AdminPanelItem(menuFactory, ADMIN_VIEW_SETTINGS_PERM) : new SimpleItem(ItemProvider.EMPTY);


        map.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue().price().doubleValue(), a.getValue().price().doubleValue()))
                .forEach(entry -> cryptoItems.add(new CryptoItem(
                        entry.getKey(), menuFactory
                )));

        return PagedGui.items()
                .setStructure(
                        "P . . . S . . . E" ,
                        ". . C C C C C . ." ,
                        "< . C C C C C . >" ,
                        ". . C C C C C . ." ,
                        ". . . . . . . . ."

                )
                .addIngredient('C', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('E', new CloseItem())
                .addIngredient('<', new BackItem(false))
                .addIngredient('>', new NextItem(true))
                .addIngredient('P', new WalletItem(player.getUniqueId(), getMenuFactory(), new GetOrCreateWalletUseCase(getWalletRepository())))
                .addIngredient('S', settingsItem)
                .setContent(cryptoItems)
                .build();
    }

    @Override
    public void playOpenSound() {

    }
}
