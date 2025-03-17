package com.playtomic.tests.wallet.infrastructure.stripe;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.CreditCard;
import com.playtomic.tests.wallet.domain.valueobject.Transaction;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.domain.valueobject.TransactionType.DEPOSIT;
import static com.playtomic.tests.wallet.domain.valueobject.TransactionType.REFUND;

public class StripePaymentService implements PaymentService {

    private final StripeService stripeService;

    public StripePaymentService(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Override
    public Transaction deposit(Wallet wallet, BigDecimal amount, CreditCard creditCard) {
        var payment = stripeService.charge(creditCard.number(), amount);
        return new Transaction(payment.getId(), amount, DEPOSIT);
    }

    @Override
    public Transaction refund(Wallet wallet, Transaction transaction) {
        var refund = stripeService.refund(transaction.id());
        return new Transaction(refund.getId(), transaction.amount(), REFUND);
    }
}
