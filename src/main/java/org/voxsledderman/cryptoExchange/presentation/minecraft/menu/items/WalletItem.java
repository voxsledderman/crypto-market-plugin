package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.application.usecases.GetOrCreateWalletUseCase;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.WalletItemProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;

import java.util.UUID;

public class WalletItem extends AutoUpdateItem {
   private final Wallet wallet;
   private final MenuFactory menuFactory;

    public WalletItem(UUID uuid, MenuFactory menuFactory, GetOrCreateWalletUseCase getOrCreateWalletUseCase) {
        this(menuFactory, getOrCreateWalletUseCase.getOrCreateWallet(uuid));
    }

    private WalletItem(MenuFactory menuFactory , Wallet wallet) {
        super(3 * 20, () -> WalletItemProvider.createProvider(menuFactory.getPriceProvider(), menuFactory.getMenuContext(), wallet));
        this.menuFactory = menuFactory;
        this.wallet = wallet;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if(wallet.getOrders().isEmpty()) return;
        Menu menu = menuFactory.createPortfolioMenu(MenuType.OPENED_POSITIONS, wallet, PositionState.OPENED);
        menu.openMenu(player);
    }
}
