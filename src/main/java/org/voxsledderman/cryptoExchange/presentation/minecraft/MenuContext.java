package org.voxsledderman.cryptoExchange.presentation.minecraft;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;

@RequiredArgsConstructor
@Builder
@Getter
public class MenuContext {
    private final AppConfigManager appConfigManager;
    private final MenuConfigManager menuConfigManager;
    private final WalletRepository walletRepository;
    private final EconomyRepository economyRepository;
}
