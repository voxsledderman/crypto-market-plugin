package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import org.bukkit.Material;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeItemProvider {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final RoundingMode ROUNDING = RoundingMode.HALF_DOWN;

    public static ItemProvider createProvider(Wallet wallet, String ticker, CryptoInfo cryptoInfo, PositionState positionState){
        String cryptoValue = PriceFormatter.formatMoney(WalletCalculator.getSingleCryptoValue(wallet, ticker, cryptoInfo, positionState));
        String roi = PriceFormatter.formatPercentage(WalletCalculator.getSingleCryptoROI(wallet, ticker, cryptoInfo, positionState).toString());
        AtomicInteger temp = new AtomicInteger(1);

        ItemBuilder builder = new ItemBuilder(Material.BOOK);
        builder.setDisplayName(cryptoInfo.fullName() + " x"  + WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, ticker, positionState));
        builder.addLoreLines("total value: %s >> %s".formatted(cryptoValue , roi));
        wallet.getOrders().get(ticker)
                .stream()
                .filter(trade -> trade.isOpened() == (positionState == PositionState.OPENED))
                .forEach(trade -> addLoreLines(trade, cryptoInfo, temp.getAndIncrement(), builder));
        return builder;
    }

    private static void addLoreLines(TradeOrder trade, CryptoInfo cryptoInfo, int index, ItemBuilder builder){
        final BigDecimal valueOnOpen = trade.getTradeValueOnOpen();
        BigDecimal profitPercent;
        BigDecimal valueNow;

        if(trade.isOpened()){
            valueNow = trade.getTradeValueNow(cryptoInfo.price());
            profitPercent = trade.getProfitOpened(cryptoInfo.price())
                    .divide(valueOnOpen, 6, ROUNDING)
                    .multiply(HUNDRED);
        } else {
            valueNow = trade.getClosedValue();
            profitPercent = trade.getProfitClosed()
                    .divide(valueOnOpen, 6, ROUNDING)
                    .multiply(HUNDRED);
        }
        builder.addLoreLines(
                "%s. %s x%s %s >> %s".formatted(index, cryptoInfo.fullName(), trade.getAmount(),
                        PriceFormatter.formatMoney(valueNow), PriceFormatter.formatPercentage(profitPercent.toString())
                )
        );
    }
}
