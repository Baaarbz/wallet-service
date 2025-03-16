package com.playtomic.tests.wallet.application.buyproduct;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public abstract class BuyProductResponse {
    private String message;

    public static class Success extends BuyProductResponse {
        public Success(String message) {
            super.message = message;
        }
    }

    public static class NotFound extends BuyProductResponse {
        public NotFound(String message) {
            super.message = message;
        }
    }
}
