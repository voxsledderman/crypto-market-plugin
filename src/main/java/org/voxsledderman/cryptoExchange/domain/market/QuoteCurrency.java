package org.voxsledderman.cryptoExchange.domain.market;

import lombok.Getter;

public enum QuoteCurrency {
    USD("USDT"),
    EURO("EUR"),
    PLN("PLN"),
    TRY("TRY"),
    GBD("GBD"),
    BRL("BRL");

    @Getter
    private final String currencyTicker;

    QuoteCurrency(String currencyTicker){
        this.currencyTicker = currencyTicker;
    }

}
