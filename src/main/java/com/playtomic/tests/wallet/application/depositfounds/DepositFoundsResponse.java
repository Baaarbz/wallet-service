package com.playtomic.tests.wallet.application.depositfounds;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public abstract class DepositFoundsResponse {
    private String message;

    public static class Success extends DepositFoundsResponse {
        public Success(String message) {
            super.message = message;
        }
    }

    public static class WalletNotFound extends DepositFoundsResponse {
        public WalletNotFound(String message) {
            super.message = message;
        }
    }
}
