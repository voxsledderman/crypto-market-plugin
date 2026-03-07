package org.voxsledderman.cryptoExchange.application.usecases;

import lombok.AllArgsConstructor;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SellCryptoUseCase {
    private final EconomyRepository economyRepository;
    private final WalletRepository walletRepository;
    private final PriceProvider priceProvider;

    public boolean sellCrypto(UUID sellerId, Wallet wallet, String ticker, BigDecimal amountToClose) {
        validateInput(sellerId, wallet, ticker, amountToClose);

        BigDecimal maxAmount = WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, ticker, PositionState.OPENED);
        if (amountToClose.compareTo(maxAmount) > 0) return false;

        List<TradeOrder> openTrades = getOpenTradesSortedByAmount(wallet, ticker);
        if (openTrades.isEmpty()) return false;

        processOrders(sellerId, wallet, openTrades, amountToClose);
        return true;
    }
    //TODO: implement TransactionManager
    private void processOrders(UUID sellerId, Wallet wallet, List<TradeOrder> openTrades, BigDecimal amountToClose) {
        for (TradeOrder trade : openTrades) {
            BigDecimal amount = trade.getAmount();

            if (amount.compareTo(amountToClose) <= 0) {
                closeTradeOrderAndGiveMoney(sellerId, wallet, trade);
                amountToClose = amountToClose.subtract(amount);
                if (amountToClose.compareTo(BigDecimal.ZERO) == 0) break;
            } else {
                TradeOrder partial = buildPartialOrder(trade, amountToClose);
                trade.setAmount(amount.subtract(amountToClose));
                wallet.addTrade(partial);
                walletRepository.save(wallet);
                closeTradeOrderAndGiveMoney(sellerId, wallet, partial);
                break;
            }
        }
    }

    private List<TradeOrder> getOpenTradesSortedByAmount(Wallet wallet, String ticker) {
        List<TradeOrder> orders = wallet.getOrders().get(ticker);
        if (orders == null) return List.of();

        return orders.stream()
                .filter(o -> o.getPositionState() == PositionState.OPENED)
                .sorted(Comparator.comparing(TradeOrder::getAmount))
                .collect(Collectors.toList());
    }

    private TradeOrder buildPartialOrder(TradeOrder source, BigDecimal amount) {
        return TradeOrder.builder()
                .ticker(source.getTicker())
                .openPrice(source.getOpenPrice())
                .positionState(source.getPositionState())
                .openTime(source.getOpenTime())
                .walletUuid(source.getWalletUuid())
                .amount(amount)
                .build();
    }

    private void validateInput(UUID sellerId, Wallet wallet, String ticker, BigDecimal amountToClose) {
        if (sellerId == null) throw new IllegalArgumentException("sellerId cannot be null");
        if (wallet == null) throw new IllegalArgumentException("Wallet cannot be null");
        if (!sellerId.equals(wallet.getOwnerUuid()))
            throw new IllegalArgumentException("Seller is not the owner of the provided wallet");
        if (ticker == null || ticker.isBlank()) throw new IllegalArgumentException("Ticker cannot be null or blank");
        if (amountToClose == null || amountToClose.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("amountToClose must be greater than zero");
    }

    private void closeTradeOrderAndGiveMoney(UUID sellerId, Wallet wallet, TradeOrder tradeOrder) {
        BigDecimal currentValue = tradeOrder.getTradeValueNow(priceProvider.getCurrentData(tradeOrder.getTicker()).price());
        if (currentValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Trade value must be greater than zero, ticker: " + tradeOrder.getTicker());
        }
        if (!economyRepository.deposit(sellerId, currentValue)) {
            throw new IllegalStateException("Failed to deposit funds for seller: " + sellerId);
        }
        try {
            tradeOrder.setPositionState(PositionState.CLOSED);
            tradeOrder.setClosedValue(currentValue);
            walletRepository.save(wallet);
        } catch (Exception e) {
            economyRepository.withdraw(sellerId, currentValue);
            tradeOrder.setPositionState(PositionState.OPENED);
            tradeOrder.setClosedValue(null);
            throw new IllegalStateException("Sell failed after deposit — funds reverted", e);
        }
    }
}