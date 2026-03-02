package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.BackItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.NextItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.TradeItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PortfolioMenu extends Menu{
    private final Wallet wallet;
    private final PriceProvider priceProvider;
    private final PositionState positionState;

    public PortfolioMenu(AppConfigManager appConfigManager, MenuType menuType,
                         MenuConfigManager menuConfigManager, WalletRepository walletRepository,
                         Wallet wallet, PriceProvider priceProvider, PositionState positionState, EconomyRepository economyRepository) {
        super(appConfigManager, menuType, menuConfigManager, walletRepository, economyRepository);
        this.wallet = wallet;
        this.priceProvider = priceProvider;
        this.positionState = positionState;
    }

    @Override
    public Gui setupGui() {

        List<Item> items = new ArrayList<>();
        Map<String, List<TradeOrder>> ordersMap = wallet.getOrders();

        var marketData = priceProvider.getFullMarketData(new ArrayList<>(ordersMap.keySet())); //TODO: test if possible

        ordersMap.keySet()
                .forEach(ticker ->
                        items.add(new TradeItem(wallet, marketData.get(ticker), ticker, positionState)));

        return PagedGui.items()
                .setStructure(
                        "P . . . . . . . w",
                        ". b T T T T T b .",
                        "B . T T T T T . N",
                        ". b T T T T T b .",
                        "E . . . . . . . w"
                )
                .addIngredient('T', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('N', new NextItem(true))
                .addIngredient('B', new BackItem(false))
                .addIngredient('w', new SimpleItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)))
                .addIngredient('b', new SimpleItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)))
                .setContent(items)
                .build();
    }

    @Override
    public void playOpenSound() {

    }
}
