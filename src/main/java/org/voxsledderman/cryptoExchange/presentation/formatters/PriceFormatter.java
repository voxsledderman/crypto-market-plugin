package org.voxsledderman.cryptoExchange.presentation.formatters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PriceFormatter {

    public static String formatMoney(double balance) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        boolean isWholeNumber = Math.abs(balance % 1) < 0.001;
        String pattern = isWholeNumber ? "#,##0" : "#,##0.00";

        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(balance) + "$";
    }

    public static String formatMoney(BigDecimal balance) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        boolean isWholeNumber = balance.stripTrailingZeros().scale() <= 0;
        String pattern = isWholeNumber ? "#,##0" : "#,##0.00";

        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(balance) + "$";
    }

    public static String formatPercentage(String percentage) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00'%'", symbols);
        return decimalFormat.format(new BigDecimal(percentage));
    }
}