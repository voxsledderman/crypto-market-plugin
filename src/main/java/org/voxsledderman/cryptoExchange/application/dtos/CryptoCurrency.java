package org.voxsledderman.cryptoExchange.application.dtos;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CryptoCurrency {
    private final String ticker;
    private String name;
    private BigDecimal currentPrice;
    private String oneDayChangePercentage;

}
