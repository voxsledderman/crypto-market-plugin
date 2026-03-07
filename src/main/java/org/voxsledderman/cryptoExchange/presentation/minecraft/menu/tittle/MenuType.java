package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle;

import lombok.Getter;

@Getter
public enum MenuType {
    MAIN("main-menu", "Crypto Exchange"),
    CLOSED_POSITIONS("portfolio-closed-positions", "Your closed positions"),
    OPENED_POSITIONS("portfolio-opened-positions", "Your opened positions"),
    ADMIN_SETTINGS("admin-settings", "Admin Settings"),
    BUY_OR_SELL_CRYPTO("buy_or_sell_crypto", "Buy or sell crypto"),
    PICK_CRYPTO_AMOUNT("pick_crypto_amount", "Pick amount");

    private final String specificPathToConfig;
    private final String defaultTitle;


    MenuType(String specificPathToConfig, String defaultTitle) {
        this.specificPathToConfig = specificPathToConfig;
        this.defaultTitle = defaultTitle;
    }
}
