package com.playtomic.tests.wallet.application.refundpurchase;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class RefundPurchaseResponse {
    private String message;

    public static class Success extends RefundPurchaseResponse {
        public Success(String message) {
            super.message = message;
        }
    }

    public static class WalletNotFound extends RefundPurchaseResponse {
        public WalletNotFound(String message) {
            super.message = message;
        }
    }
}
