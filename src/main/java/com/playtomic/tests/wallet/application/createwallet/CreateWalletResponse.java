package com.playtomic.tests.wallet.application.createwallet;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public abstract class CreateWalletResponse {
    private String message;

    public static class Success extends CreateWalletResponse {
        public Success(String message) {
            super.message = message;
        }
    }
}
