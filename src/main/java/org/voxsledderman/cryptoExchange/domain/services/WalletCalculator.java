package org.voxsledderman.cryptoExchange.domain.services;

import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class WalletCalculator {

    public static BigDecimal getTotalEarnings(Wallet wallet, Map<String, CryptoInfo> currentCryptoInfo) {
        return wallet.getOrders().values().stream()
                .flatMap(List::stream)
                .map(trade -> {
                    BigDecimal currentPrice = currentCryptoInfo.get(trade.getTicker()).price();
                    if (currentPrice == null) return BigDecimal.ZERO;
                    return trade.getProfit(currentPrice);
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getPortfolioValue(Wallet wallet, Map<String, CryptoInfo> currentCryptoInfo) {
        return wallet.getOrders().values().stream()
                .flatMap(List::stream)
                .map(trade -> trade.getTradeValueNow(currentCryptoInfo.get(trade.getTicker()).price()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getROI(Wallet wallet, Map<String, CryptoInfo> currentCryptoInfo) {
        BigDecimal totalEarnings = getTotalEarnings(wallet, currentCryptoInfo);
        BigDecimal portfolioValue = getPortfolioValue(wallet, currentCryptoInfo);
        BigDecimal invested = portfolioValue.subtract(totalEarnings);

        if (invested.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return totalEarnings
                .divide(invested, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
    public static BigDecimal getSingleCryptoValue(Wallet wallet, String ticker, CryptoInfo cryptoInfo){
        return wallet.getOrders().get(ticker).stream()
                .map(trade -> trade.getTradeValueNow(cryptoInfo.price()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public static BigDecimal getSingleCryptoEarnings(Wallet wallet, String ticker, CryptoInfo cryptoInfo){
        return wallet.getOrders().get(ticker).stream()
                .map(trade -> trade.getOpenPrice().subtract(trade.getTradeValueNow(cryptoInfo.price())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getSingleCryptoROI(Wallet wallet, String ticker, CryptoInfo cryptoInfo){
        BigDecimal totalEarnings = getSingleCryptoEarnings(wallet ,ticker, cryptoInfo);
        BigDecimal cryptoValue = getSingleCryptoValue(wallet, ticker, cryptoInfo);
        BigDecimal invested = cryptoValue.subtract(totalEarnings);

        if (invested.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return totalEarnings.
                divide(invested, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public static List<TradeOrder> filterOrdersByPositionState(Wallet wallet, PositionState positionState){
        return wallet.getOrders().values().stream()
                .flatMap(List::stream)
                .filter(order -> order.getPositionState().equals(positionState))
                .toList();
    }
}
