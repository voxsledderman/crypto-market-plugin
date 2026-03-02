package org.voxsledderman.cryptoExchange.application.usecases;

import lombok.AllArgsConstructor;
import org.voxsledderman.cryptoExchange.application.dtos.CryptoAssetDto;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
public class BuyCryptoUseCase {
    private final WalletRepository walletRepository;
    private final EconomyRepository economyRepository;
    private final AppConfigManager appConfigManager;

    public boolean buyCrypto(UUID buyerId, Wallet wallet, CryptoAssetDto dto){
        if(!buyerId.equals(wallet.getOwnerUuid())){
            throw new IllegalArgumentException("buyer is not the owner of provided wallet!");
        }
        BigDecimal totalCost = dto.assetValue().add((dto.assetValue().multiply(appConfigManager.getSpread())));

        if(totalCost.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Provided CryptoAsset value is equal or smaller than 0!");
        }

        if (!economyRepository.withdraw(buyerId, totalCost)) {
            throw new IllegalStateException("Could not withdraw funds for buyer: " + buyerId);
        }

        try {
            wallet.addTrade(new TradeOrder(
                    null,
                    dto.ticker(),
                    buyerId, dto.amount(),
                    dto.currentPricePerUnit(),
                    LocalDateTime.now(),
                    PositionState.OPENED
            ));
            walletRepository.save(wallet);
        } catch (Exception e){
            economyRepository.deposit(buyerId, totalCost);
            throw new IllegalStateException("Buy failed after withdrawal, funds reverted", e);
        }

        return true;
    }
}
