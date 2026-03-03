package org.voxsledderman.cryptoExchange.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
    private UUID ownerUuid;
    private Map<String, List<TradeOrder>> orders;


    public void addTrade(TradeOrder tradeOrder){
        orders.computeIfAbsent(tradeOrder.getTicker(), k -> new ArrayList<>()).add(tradeOrder);
    }
    public void removeTrade(TradeOrder tradeOrder) throws IllegalArgumentException {
        String ticker = tradeOrder.getTicker();
        if(!orders.containsKey(ticker) || !orders.get(ticker).contains(tradeOrder)){
            throw new IllegalArgumentException("Wallet doesn't contain this TradeOrder");
        }
        orders.get(ticker).remove(tradeOrder);
    }
}
