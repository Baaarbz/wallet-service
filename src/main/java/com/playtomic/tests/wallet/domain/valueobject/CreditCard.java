package com.playtomic.tests.wallet.domain.valueobject;

public record CreditCard(String number) {
    public CreditCard {
        if (number == null) {
            throw new IllegalArgumentException("Credit card number cannot be null");
        }
    }
}
