package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeItem extends AbstractItem {
    private final Wallet wallet;
    private final PositionState positionState;
    private final CryptoInfo cryptoInfo;
    private final String ticker;;

    public TradeItem(Wallet wallet, CryptoInfo cryptoInfo, String ticker, PositionState positionState) {
        this.wallet = wallet;
        this.positionState = positionState;
        this.cryptoInfo = cryptoInfo;
        this.ticker = ticker;
    }

    @Override
    public ItemProvider getItemProvider(){
        String cryptoValue = PriceFormatter.formatMoney(WalletCalculator.getSingleCryptoValue(wallet, ticker, cryptoInfo));
        String roi = PriceFormatter.formatPercentage(WalletCalculator.getSingleCryptoROI(wallet, ticker, cryptoInfo).toString());
        AtomicInteger temp = new AtomicInteger(1);

        ItemBuilder builder = new ItemBuilder(Material.BOOK);
        builder.setDisplayName(cryptoInfo.fullName() + " x"  + WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, ticker, positionState));
        builder.addLoreLines(
                "total value: %s - (%s)".formatted(
                       cryptoValue , roi));
        wallet.getOrders().get(ticker)
                .forEach(
                        trade -> {
                            if(trade.getPositionState().equals(positionState)) {
                                var currentValue = trade.getTradeValueNow(cryptoInfo.price());
                                builder.addLoreLines(
                                        "%s. %s x%s %s >> %s".formatted(temp.getAndIncrement(), cryptoInfo.fullName(), trade.getAmount() ,PriceFormatter.formatMoney(currentValue)
                                                , PriceFormatter.formatPercentage(trade.getProfit(currentValue.divide(trade.getAmount(), RoundingMode.HALF_DOWN)).toString()))
                                );
                            }
                        }
                );
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {}
}
