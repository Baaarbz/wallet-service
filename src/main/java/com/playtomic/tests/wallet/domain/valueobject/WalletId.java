package com.playtomic.tests.wallet.domain.valueobject;

import java.util.UUID;

public record WalletId(String value) {
    public WalletId {
        if (value == null) {
            throw new IllegalArgumentException("WalletId cannot be null");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("WalletId cannot be empty");
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("WalletId must be a valid UUID");
        }
    }
}
