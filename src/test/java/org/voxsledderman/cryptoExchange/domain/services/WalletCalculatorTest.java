package org.voxsledderman.cryptoExchange.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class WalletCalculatorTest {

    private Wallet wallet;
    private Map<String, CryptoInfo> cryptoInfoMap;


    @BeforeEach
    void setUpTest(){
        TradeOrder btcOrder = TradeOrder.builder()
                .ticker("BTC")
                .openPrice(BigDecimal.valueOf(100))
                .amount(BigDecimal.valueOf(0.2))
                .positionState(PositionState.OPENED)
                .build();

        TradeOrder ethOrder = TradeOrder.builder()
                .ticker("ETH")
                .openPrice(BigDecimal.valueOf(10))
                .amount(BigDecimal.valueOf(2))
                .positionState(PositionState.OPENED)
                .build();

        TradeOrder solanaOrder = TradeOrder.builder()
                .ticker("SOL")
                .openPrice(BigDecimal.valueOf(50))
                .amount(BigDecimal.valueOf(2))
                .positionState(PositionState.CLOSED)
                .build();

        wallet = Wallet.builder()
                .ownerUuid(UUID.randomUUID())
                .orders(new HashMap<>(Map.of(
                        "BTC", new ArrayList<>(List.of(btcOrder)),
                        "ETH", new ArrayList<>(List.of(ethOrder)),
                        "SOL", new ArrayList<>(List.of(solanaOrder))
                )))
                .build();

        cryptoInfoMap = Map.of(
                "BTC", new CryptoInfo("Bitcoin", BigDecimal.valueOf(200), "20"),
                "ETH", new CryptoInfo("Ethereum", BigDecimal.valueOf(5), "-10"),
                "SOL", new CryptoInfo("Solana", BigDecimal.valueOf(100), "50")
        );

    }


    @Test
    void shouldCalculateEarningsCorrectly(){
        var result = WalletCalculator.getTotalCurrentEarnings(wallet, cryptoInfoMap);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(10)));
    }

    @Test
    void shouldHandleNullCurrentPriceInTotalEarnings() {
        Map<String, CryptoInfo> incompleteMap = new HashMap<>();
        incompleteMap.put("BTC", new CryptoInfo("Bitcoin", null, "0"));
        var result = WalletCalculator.getTotalCurrentEarnings(wallet, incompleteMap);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldCalculatePortfolioValueCorrectly(){
        var result = WalletCalculator.getCurrentPortfolioValue(wallet, cryptoInfoMap);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(50)));
    }

    @Test
    void shouldCalculateROICorrectly(){
        var result = WalletCalculator.getCurrentROI(wallet, cryptoInfoMap);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(25)));
    }
    @Test
    void shouldCalculateSingleCryptoValueCorrectly() {
        var btcInfo = cryptoInfoMap.get("BTC");
        var result = WalletCalculator.getSingleCryptoValue(wallet, "BTC", btcInfo, PositionState.OPENED);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(40)));
    }

    @Test
    void shouldCalculateSingleCryptoEarningsCorrectly() {
        var ethInfo = cryptoInfoMap.get("ETH");
        var result = WalletCalculator.getSingleCryptoEarnings(wallet, "ETH", ethInfo);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(-10)));
    }

    @Test
    void shouldCalculateSingleCryptoROICorrectly() {
        var btcInfo = cryptoInfoMap.get("BTC");
        var result = WalletCalculator.getSingleCryptoROI(wallet, "BTC", btcInfo, PositionState.OPENED);
        assertEquals(0, result.compareTo(new BigDecimal("100.000000")));
    }

    @Test
    void shouldFilterOrdersByPositionStateCorrectly() {
        var openedOrders = WalletCalculator.filterOrdersByPositionState(wallet, PositionState.OPENED);
        var closedOrders = WalletCalculator.filterOrdersByPositionState(wallet, PositionState.CLOSED);

        assertEquals(2, openedOrders.size());
        assertEquals(1, closedOrders.size());
        assertEquals("SOL", closedOrders.getFirst().getTicker());
    }

    @Test
    void shouldReturnZeroROIWhenNoInvestment() {
        Wallet emptyWallet = Wallet.builder()
                .ownerUuid(UUID.randomUUID())
                .orders(new HashMap<>())
                .build();

        var result = WalletCalculator.getCurrentROI(emptyWallet, cryptoInfoMap);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldHandleMissingTickerInCurrentCryptoInfo() {
        Map<String, CryptoInfo> incompleteMap = Map.of(
                "BTC", new CryptoInfo("Bitcoin", BigDecimal.valueOf(200), "20")
        );

        var result = WalletCalculator.getTotalCurrentEarnings(wallet, incompleteMap);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(20)));
    }

    @Test
    void shouldHandleTickerNotPresentInWallet() {
        CryptoInfo dogeInfo = new CryptoInfo("Dogecoin", BigDecimal.valueOf(1), "5");
        var result = WalletCalculator.getSingleCryptoValue(wallet, "DOGE", dogeInfo, PositionState.OPENED);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldHandleNullPriceInCryptoInfo() {
        Map<String, CryptoInfo> nullPriceMap = Map.of(
                "BTC", new CryptoInfo("Bitcoin", null, "0"),
                "ETH", new CryptoInfo("Ethereum", BigDecimal.valueOf(5), "-10")
        );

        var result = WalletCalculator.getCurrentPortfolioValue(wallet, nullPriceMap);

        assertEquals(0, result.compareTo(BigDecimal.valueOf(10)));
    }

    @Test
    void shouldReturnZeroROIWhenInvestedAmountIsZero() {
        TradeOrder freeOrder = TradeOrder.builder()
                .ticker("FREE")
                .openPrice(BigDecimal.ZERO)
                .amount(BigDecimal.valueOf(10))
                .positionState(PositionState.OPENED)
                .build();

        Wallet freeWallet = Wallet.builder()
                .ownerUuid(UUID.randomUUID())
                .orders(new HashMap<>(Map.of("FREE", new ArrayList<>(List.of(freeOrder)))))
                .build();

        Map<String, CryptoInfo> freeMap = Map.of(
                "FREE", new CryptoInfo("FreeCoin", BigDecimal.valueOf(10), "0")
        );

        var result = WalletCalculator.getCurrentROI(freeWallet, freeMap);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldHandleEmptyWalletInFiltering() {
        Wallet emptyWallet = Wallet.builder()
                .ownerUuid(UUID.randomUUID())
                .orders(null)
                .build();

        var result = WalletCalculator.filterOrdersByPositionState(emptyWallet, PositionState.OPENED);
        assertEquals(0, result.size());
    }

    @Test
    void shouldHandleNullElementsInsideOrderList() {
        wallet.getOrders().get("BTC").add(null);

        var result = WalletCalculator.getTotalCurrentEarnings(wallet, cryptoInfoMap);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(10)));
    }

    @Test
    void shouldHandleTradeOrderWithNullTicker() {
        TradeOrder brokenOrder = TradeOrder.builder()
                .ticker(null)
                .openPrice(BigDecimal.valueOf(100))
                .amount(BigDecimal.valueOf(1))
                .positionState(PositionState.OPENED)
                .build();

        wallet.getOrders().put("UNKNOWN", new ArrayList<>(List.of(brokenOrder)));

        var result = WalletCalculator.getTotalCurrentEarnings(wallet, cryptoInfoMap);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(10)));
    }

    @Test
    void shouldHandleNullCryptoInfoInSingleMethods() {
        var result = WalletCalculator.getSingleCryptoValue(wallet, "BTC", null, PositionState.OPENED);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
    @Test
    void shouldCalculateTotalAmountOfCryptoAcquiredCorrectly() {
        var result = WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, "BTC", PositionState.OPENED);

        assertEquals(0, result.compareTo(BigDecimal.valueOf(0.2)));
    }

    @Test
    void shouldReturnZeroAmountForNonExistentTicker() {
        var result = WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, "DOGE", PositionState.OPENED);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
}