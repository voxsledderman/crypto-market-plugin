package org.voxsledderman.cryptoExchange.domain.market;

import java.util.List;
import java.util.Map;

public interface PriceProvider {
    CryptoInfo getCurrentData(String ticker);
    Map<String, CryptoInfo> getFullMarketData(List<String> tickers);
}
