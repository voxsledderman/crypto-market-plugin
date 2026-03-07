package org.voxsledderman.cryptoExchange.infrastructure.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.knowm.xchange.currency.Currency;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.market.QuoteCurrency;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class BinanceWebSocketProvider implements PriceProvider {

    private final Map<String, CryptoInfo> latestPrices = new ConcurrentHashMap<>();
    private final Map<String, String> latestChanges = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private WebSocketClient client;
    private volatile boolean running = true;
    private final QuoteCurrency quoteCurrency;

    public BinanceWebSocketProvider(List<String> tickers, QuoteCurrency quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
        initConnection(tickers);
    }

    private void initConnection(List<String> tickers) {
        try {
            StringBuilder urlBuilder = new StringBuilder("wss://stream.binance.com:9443/stream?streams=");
            for (int i = 0; i < tickers.size(); i++) {
                urlBuilder.append(tickers.get(i).toLowerCase()).append("@ticker");
                if (i < tickers.size() - 1) urlBuilder.append("/");
            }

            this.client = new WebSocketClient(new URI(urlBuilder.toString())) {
                @Override
                public void onOpen(ServerHandshake handShakeData) {
                    log.info("Connected with Binance Websocket successfully");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        JsonNode root = objectMapper.readTree(message);
                        JsonNode data = root.get("data");

                        String symbol = data.get("s").asText().toUpperCase();
                        BigDecimal price = new BigDecimal(data.get("c").asText());
                        String change = data.get("P").asText();

                        latestPrices.put(symbol, new CryptoInfo(
                                Currency.getInstance(symbol.replace(quoteCurrency.getCurrencyTicker(), "")).getDisplayName(),
                                price, change
                        ));
                        latestChanges.put(symbol, change);
                    } catch (Exception e) {
                        log.error("Parsing Error: {}", e.getMessage());
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.error("Connection interrupted: {}, Reason: {}", code, reason);

                    if (!running) return;

                    new Thread(() -> {
                        try {
                            log.info("Trying to reconnect in 5 seconds...");
                            Thread.sleep(5000);
                            if (running) reconnect();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };

            client.connect();

        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize Websocket", e);
        }
    }

    public void shutdown() {
        running = false;
        if (client != null) {
            client.close();
        }
    }

    public Map<String, BigDecimal> getPrices(List<String> tickers) {
        if (tickers == null || tickers.isEmpty()) {
            return new HashMap<>();
        }

        return tickers.stream()
                .map(String::toUpperCase)
                .filter(latestPrices::containsKey)
                .collect(Collectors.toMap(
                        ticker -> ticker,
                        ticker -> latestPrices.get(ticker).price()
                ));
    }

    public Map<String, CryptoInfo> getFullMarketData(List<String> tickers) {
        return tickers.stream()
                .map(String::toUpperCase)
                .filter(latestPrices::containsKey)
                .collect(Collectors.toMap(
                        ticker -> ticker,
                        ticker -> new CryptoInfo(Currency.getInstance(ticker.replace(quoteCurrency.getCurrencyTicker(), ""))
                                .getDisplayName(), latestPrices.get(ticker).price(), latestChanges.get(ticker))
                ));
    }

    @Override
    public CryptoInfo getCurrentData(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cant be null or blank");
        }

        CryptoInfo info = latestPrices.get(ticker.toUpperCase());

        if (info == null) {
            throw new IllegalArgumentException("Unknown ticker: [%s]".formatted(ticker));
        }
        return info;
    }
}