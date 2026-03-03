package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.application.usecases.BuyCryptoUseCase;
import org.voxsledderman.cryptoExchange.application.usecases.SellCryptoUseCase;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.BuyItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.CryptoItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.PickAmountItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.SellItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;

import java.math.BigDecimal;

public class BuySellCryptoMenu extends Menu{
    private final CryptoItem cryptoItem;
    private final BigDecimal pickedAmount;
    private final PriceProvider priceProvider;
    private final Wallet wallet;
    private final String ticker;

    public BuySellCryptoMenu(MenuContext menuContext, MenuType menuType, CryptoItem cryptoItem, BigDecimal pickedAmount, JavaPlugin plugin, PriceProvider priceProvider, Wallet wallet, String ticker) {
        super(plugin, menuContext, menuType);
        this.cryptoItem = cryptoItem;
        this.pickedAmount = pickedAmount;
        this.priceProvider = priceProvider;
        this.wallet = wallet;
        this.ticker = ticker;
    }

    @Override
    public Gui setupGui() {
        return Gui.normal()
                .setStructure(
                        "b b b b . s s s s",
                        "b b b b C s s s s",
                        ". b b b H s s s ."
                )
                .addIngredient('C', cryptoItem)
                .addIngredient('H', new PickAmountItem(pickedAmount, getPlugin(), getMenuContext(), cryptoItem, ticker))
                .addIngredient('b', new BuyItem(new BuyCryptoUseCase(
                        getWalletRepository(), getEconomyRepository(), getAppConfigManager()
                ), pickedAmount, getPlugin(), wallet, ticker, priceProvider.getCurrentData(ticker)))
                .addIngredient('s', new SellItem(new SellCryptoUseCase(
                        getEconomyRepository(), getWalletRepository(), priceProvider),
                        pickedAmount, getPlugin(), wallet, ticker, priceProvider.getCurrentData(ticker)
                ))
                .build();

    }

    @Override
    public void playOpenSound() {

    }
}
