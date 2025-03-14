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

public record Wallet(
        WalletId id,
        CreditCard associatedCreditCard,
        Balance balance,
        List<Transaction> transactions
) {

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
        if (associatedCreditCard == null) {
            throw new IllegalArgumentException("Wallet associated credit card cannot be null");
        }
    }

    public Wallet(String id, String creditCardNumber) {
        this(new WalletId(id), new CreditCard(creditCardNumber), new Balance(BigDecimal.ZERO), List.of());
    }

    public Wallet deposit(BigDecimal amount, PaymentService paymentService) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative");
        }

        var depositTransaction = paymentService.deposit(this, amount);

        return applyTransaction(depositTransaction);
    }

    public Wallet refund(Transaction transaction, PaymentService paymentService) {
        var transactionToRefund = transactions.stream()
                .filter(t -> t.id().equals(transaction.id()))
                .findFirst();

        if (transactionToRefund.isEmpty()) {
            throw new IllegalArgumentException("Transaction not found to refund");
        }

        var refundTransaction = paymentService.refund(this, transactionToRefund.get());

        return applyTransaction(refundTransaction);
    }

    public Wallet buy(BigDecimal price, PaymentService paymentService) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The price of the service/product cannot be negative");
        }

        var buyTransaction = new Transaction(UUID.randomUUID().toString(), price.negate(), BUY);

        return applyTransaction(buyTransaction);
    }

    private Wallet applyTransaction(Transaction transaction) {
        var updatedTransactions = Stream.concat(transactions.stream(), Stream.of(transaction)).toList();
        return new Wallet(id, associatedCreditCard, new Balance(balance.value().add(transaction.amount())), updatedTransactions);
    }
}
