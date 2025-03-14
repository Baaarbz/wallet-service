package com.playtomic.tests.wallet.domain.valueobject;

import java.math.BigDecimal;

public record Balance (BigDecimal value) {
    public Balance {
        if (value == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }
}
