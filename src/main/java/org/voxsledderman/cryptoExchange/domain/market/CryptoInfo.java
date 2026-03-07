package org.voxsledderman.cryptoExchange.domain.market;

import java.math.BigDecimal;

public record CryptoInfo(String fullName, BigDecimal price, String changePercent) {}

