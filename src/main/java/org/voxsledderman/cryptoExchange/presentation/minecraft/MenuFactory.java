package org.voxsledderman.cryptoExchange.presentation.minecraft;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.*;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.CryptoItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class MenuFactory {
    private final MenuContext menuContext;
    private final JavaPlugin plugin;
    private final PriceProvider priceProvider;

    public MenuFactory(MenuContext menuContext, JavaPlugin plugin, PriceProvider priceProvider) {
        this.menuContext = menuContext;
        this.plugin = plugin;
        this.priceProvider = priceProvider;
    }

    public MainMenu createMainMenu(Player player) {
        return new MainMenu(player, this);
    }

    public PortfolioMenu createPortfolioMenu(MenuType menuType, Wallet wallet, PositionState positionState) {
        return new PortfolioMenu(menuType, wallet, positionState, this);
    }

    public BuySellCryptoMenu createBuySellCryptoMenu(CryptoItem cryptoItem, BigDecimal amount, Wallet wallet, Menu backMenu) {
        return new BuySellCryptoMenu(MenuType.BUY_OR_SELL_CRYPTO, cryptoItem, amount, wallet, backMenu, this);
    }
    public AnvilAmountPicker createAnvilAmountPickerMenu(){
        return new AnvilAmountPicker(this, getPlugin(), menuContext.getMenuConfigManager());
    }
    public AdminPanelMenu createAdminPanelMenu(UUID adminUuid) {
        return new AdminPanelMenu(MenuType.ADMIN_PANEL, this, adminUuid, menuContext);
    }
}
