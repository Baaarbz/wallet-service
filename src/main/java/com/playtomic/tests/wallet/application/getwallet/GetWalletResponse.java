package com.playtomic.tests.wallet.application.getwallet;

import com.playtomic.tests.wallet.domain.Wallet;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
public abstract class GetWalletResponse {
    @Getter
    public static class Success extends GetWalletResponse {
        private final String walletId;
        private final BigDecimal balance;
        private final String associatedCreditCard;
        private final List<TransactionResponse> transactions;

        public Success(Wallet wallet) {
            this.walletId = wallet.id().value();
            this.balance = wallet.balance().value();
            this.associatedCreditCard = wallet.associatedCreditCard().number();
            this.transactions = wallet.transactions().stream().map(TransactionResponse::new).collect(Collectors.toList());
        }
    }

    public record TransactionResponse(String id, BigDecimal amount, String type) {
        public TransactionResponse(com.playtomic.tests.wallet.domain.valueobject.Transaction transaction) {
            this(transaction.id(), transaction.amount(), transaction.type().name());
        }
    }

    @Getter
    public static class NotFound extends GetWalletResponse {
        private String message;

        public NotFound(String message) {
            this.message = message;
        }
    }
}
