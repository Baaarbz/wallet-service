package com.playtomic.tests.wallet.domain.valueobject;

public record CreditCard(String number) {
    public CreditCard {
        if (number == null) {
            throw new IllegalArgumentException("Credit card number cannot be null");
        }
        if (number.length() != 16) {
            throw new IllegalArgumentException("Credit card number must be 16 digits long");
        }
    }
}
