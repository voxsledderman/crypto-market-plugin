package org.voxsledderman.cryptoExchange.domain.services;

import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WalletCalculator {

    private WalletCalculator() {}

    public static BigDecimal getTotalCurrentEarnings(Wallet wallet, Map<String, CryptoInfo> currentCryptoInfo) {
        if (wallet.getOrders() == null || currentCryptoInfo == null) return BigDecimal.ZERO;

        return wallet.getOrders().values().stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(trade -> {
                    if (trade == null || trade.getTicker() == null) return BigDecimal.ZERO;
                    CryptoInfo info = currentCryptoInfo.get(trade.getTicker());
                    if (info == null || info.price() == null || trade.getPositionState() != PositionState.OPENED) {
                        return BigDecimal.ZERO;
                    }
                    return trade.getProfitOpened(info.price());
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getCurrentPortfolioValue(Wallet wallet, Map<String, CryptoInfo> currentCryptoInfo) {
        if (wallet.getOrders() == null || currentCryptoInfo == null) return BigDecimal.ZERO;

        return wallet.getOrders().values().stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(trade -> {
                    if (trade == null || trade.getPositionState() != PositionState.OPENED) return BigDecimal.ZERO;
                    CryptoInfo info = currentCryptoInfo.get(trade.getTicker());
                    if (info == null || info.price() == null) return BigDecimal.ZERO;

                    BigDecimal price = trade.getTradeValueNow(info.price());
                    return (price == null) ? BigDecimal.ZERO : price;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getCurrentROI(Wallet wallet, Map<String, CryptoInfo> currentCryptoInfo) {
        BigDecimal totalEarnings = getTotalCurrentEarnings(wallet, currentCryptoInfo);
        BigDecimal portfolioValue = getCurrentPortfolioValue(wallet, currentCryptoInfo);
        BigDecimal invested = portfolioValue.subtract(totalEarnings);

        if (invested.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;

        return totalEarnings
                .divide(invested, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public static BigDecimal getSingleCryptoValue(Wallet wallet, String ticker, CryptoInfo cryptoInfo, PositionState positionState) {
        if (wallet.getOrders() == null || cryptoInfo == null || cryptoInfo.price() == null) return BigDecimal.ZERO;

        return wallet.getOrders().getOrDefault(ticker, List.of()).stream()
                .filter(Objects::nonNull)
                .filter(trade -> trade.getPositionState() == positionState)
                .map(trade -> {
                    if(positionState == PositionState.OPENED) return trade.getTradeValueNow(cryptoInfo.price());
                    else return trade.getClosedValue();
                })
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getSingleCryptoEarnings(Wallet wallet, String ticker, CryptoInfo cryptoInfo) {
        if (wallet.getOrders() == null || cryptoInfo == null || cryptoInfo.price() == null) return BigDecimal.ZERO;

        return wallet.getOrders().getOrDefault(ticker, List.of()).stream()
                .filter(Objects::nonNull)
                .filter(trade -> trade.getPositionState() == PositionState.OPENED)
                .map(trade -> trade.getProfitOpened(cryptoInfo.price()))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getSingleCryptoROI(Wallet wallet, String ticker, CryptoInfo cryptoInfo, PositionState positionState) {
        BigDecimal cryptoValue = getSingleCryptoValue(wallet, ticker, cryptoInfo, positionState);

        BigDecimal invested = wallet.getOrders().getOrDefault(ticker, List.of()).stream()
                .filter(Objects::nonNull)
                .filter(trade -> trade.getPositionState() == positionState)
                .map(TradeOrder::getTradeValueOnOpen)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (invested.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;

        BigDecimal earnings = cryptoValue.subtract(invested);
        return earnings
                .divide(invested, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public static List<TradeOrder> filterOrdersByPositionState(Wallet wallet, PositionState positionState) {
        if (wallet.getOrders() == null) return List.of();
        return wallet.getOrders().values().stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(order -> order != null && order.getPositionState() == positionState)
                .toList();
    }
    public static BigDecimal getTotalAmountOfCryptoAcquired(Wallet wallet, String ticker, PositionState positionState) {
        if (wallet == null || wallet.getOrders() == null) return BigDecimal.ZERO;

        return wallet.getOrders().getOrDefault(ticker, List.of()).stream()
                .filter(Objects::nonNull)
                .filter(order -> order.getPositionState().equals(positionState))
                .map(TradeOrder::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
