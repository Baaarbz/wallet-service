package com.playtomic.tests.wallet.application.createwallet;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public abstract class CreateWalletResponse {

    @Getter
    public static class Success extends CreateWalletResponse {
        private final String walletId;
        public Success(String walletId) {
            this.walletId = walletId;
        }
    }
}
