package com.playtomic.tests.wallet.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record Transaction(
        String id,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime occurredOn
) {
    public Transaction {
        if (id == null) {
            throw new IllegalArgumentException("Transaction id cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Transaction amount cannot be null");
        }
        if (occurredOn == null) {
            throw new IllegalArgumentException("Transaction occurredOn cannot be null");
        }
    }

    public Transaction(String id, BigDecimal amount, TransactionType type) {
        this(id, amount, type, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }
}
