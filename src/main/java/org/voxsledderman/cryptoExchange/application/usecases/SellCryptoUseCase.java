package org.voxsledderman.cryptoExchange.application.usecases;

import lombok.AllArgsConstructor;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class SellCryptoUseCase {
    private final EconomyRepository economyRepository;
    private final WalletRepository walletRepository;
    private final PriceProvider priceProvider;

    public boolean sellCrypto(UUID sellerId, Wallet wallet, TradeOrder tradeOrder) {
        if (!sellerId.equals(wallet.getOwnerUuid())) {
            throw new IllegalArgumentException("seller is not the owner of provided wallet!");
        }
        BigDecimal totalValue = tradeOrder.getTradeValueNow(priceProvider.getCurrentData(tradeOrder.getTicker()).price());
        if (totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Provided CryptoAsset value is equal or smaller than 0!");
        }

        if(!economyRepository.deposit(sellerId, totalValue)){
            throw new IllegalStateException("Could not deposit funds for seller: " + sellerId);
        }

        try {
            tradeOrder.setPositionState(PositionState.CLOSED);
            walletRepository.save(wallet);
        } catch (Exception e){
            economyRepository.withdraw(sellerId, totalValue);
            throw new IllegalStateException("Sell failed after deposit, funds reverted", e);
        }
        return true;
    }
}
