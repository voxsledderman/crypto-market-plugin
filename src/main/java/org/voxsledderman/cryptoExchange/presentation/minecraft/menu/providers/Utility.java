package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import lombok.extern.slf4j.Slf4j;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;

import java.math.BigDecimal;
@Slf4j
public class Utility {
    public static String getChangePercentSuffix(String changePercent) {
        BigDecimal change;
        try {
            change = new BigDecimal(changePercent);
        } catch (NumberFormatException e) {
            log.error("Invalid change percent: {}", changePercent);
            return changePercent;
        }

        if(change.compareTo(BigDecimal.ZERO) == 0) {
            return "<white>%s ●".formatted(PriceFormatter.formatPercentage(changePercent));
        } else if(change.compareTo(BigDecimal.ZERO) > 0) {
            return "<green>%s ⬆".formatted(PriceFormatter.formatPercentage(changePercent));
        } else if(change.compareTo(BigDecimal.ZERO) < 0) {
            return "<red>%s ⬇".formatted(PriceFormatter.formatPercentage(changePercent));
        }
        return null;
    }

    public static String getChangePercentSuffix(BigDecimal changePercent) {
        if(changePercent.compareTo(BigDecimal.ZERO) == 0) {
            return "<white>%s ●".formatted(PriceFormatter.formatPercentage(changePercent.toString()));
        } else if(changePercent.compareTo(BigDecimal.ZERO) > 0) {
            return "<green>%s ⬆".formatted(PriceFormatter.formatPercentage(changePercent.toString()));
        } else if(changePercent.compareTo(BigDecimal.ZERO) < 0) {
            return "<red>%s ⬇".formatted(PriceFormatter.formatPercentage(changePercent.toString()));
        }
        return null;
    }
}
