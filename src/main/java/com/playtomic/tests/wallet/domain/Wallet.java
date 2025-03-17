package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.Balance;
import com.playtomic.tests.wallet.domain.valueobject.CreditCard;
import com.playtomic.tests.wallet.domain.valueobject.Transaction;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.playtomic.tests.wallet.domain.valueobject.TransactionType.BUY;
import static com.playtomic.tests.wallet.domain.valueobject.TransactionType.REFUND;

public record Wallet(
        WalletId id,
        Balance balance,
        List<Transaction> transactions
) {

    private static final BigDecimal MIN_AMOUNT_TO_DEPOSIT = BigDecimal.TEN;

    public Wallet {
        if (id == null) {
            throw new IllegalArgumentException("Wallet id cannot be null");
        }
        if (balance == null) {
            throw new IllegalArgumentException("Wallet balance cannot be null");
        }
        if (transactions == null) {
            throw new IllegalArgumentException("Wallet transactions cannot be null");
        }
    }

    public Wallet() {
        this(new WalletId(UUID.randomUUID().toString()), new Balance(BigDecimal.ZERO), List.of());
    }

    public Wallet deposit(BigDecimal amount, PaymentService paymentService, CreditCard creditCard) {
        if (amount.compareTo(MIN_AMOUNT_TO_DEPOSIT) < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be less than " + MIN_AMOUNT_TO_DEPOSIT.doubleValue());
        }

        var depositTransaction = paymentService.deposit(this, amount, creditCard);

        return applyTransaction(depositTransaction);
    }

    public Wallet refund(String transactionId) {
        var transactionToRefund = transactions.stream()
                .filter(t -> t.id().equals(transactionId))
                .findFirst();

        if (transactionToRefund.isEmpty()) {
            throw new IllegalArgumentException("Transaction not found to refund");
        }

        if (transactionToRefund.get().type() != BUY) {
            throw new IllegalArgumentException("Only transactions of type BUY can be refunded");
        }

        var refundTransaction = new Transaction(UUID.randomUUID().toString(), transactionToRefund.get().amount().negate(), REFUND);

        return applyTransaction(refundTransaction);
    }

    public Wallet buy(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("The price of the service/product cannot be null");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The price of the service/product cannot be negative");
        }

        if (balance.value().compareTo(price) < 0) {
            throw new IllegalArgumentException("Insufficient funds to buy the service/product");
        }

        var buyTransaction = new Transaction(UUID.randomUUID().toString(), price.negate(), BUY);

        return applyTransaction(buyTransaction);
    }

    private Wallet applyTransaction(Transaction transaction) {
        var updatedTransactions = Stream.concat(transactions.stream(), Stream.of(transaction)).toList();
        return new Wallet(id, new Balance(balance.value().add(transaction.amount())), updatedTransactions);
    }
}
