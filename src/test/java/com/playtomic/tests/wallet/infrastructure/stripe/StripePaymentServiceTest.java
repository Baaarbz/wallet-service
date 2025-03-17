package com.playtomic.tests.wallet.infrastructure.stripe;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.valueobject.CreditCard;
import com.playtomic.tests.wallet.domain.valueobject.Transaction;
import com.playtomic.tests.wallet.infrastructure.stripe.dto.Payment;
import com.playtomic.tests.wallet.infrastructure.stripe.dto.Refund;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.playtomic.tests.wallet.domain.valueobject.TransactionType.DEPOSIT;
import static com.playtomic.tests.wallet.domain.valueobject.TransactionType.REFUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StripePaymentServiceTest {
    private final StripeService stripeService = mock();
    private final StripePaymentService stripePaymentService = new StripePaymentService(stripeService);

    @Test
    void deposit_shouldReturnTransaction() {
        BigDecimal amount = BigDecimal.valueOf(100);
        Payment payment = new Payment(UUID.randomUUID().toString());
        when(stripeService.charge(any(String.class), any(BigDecimal.class))).thenReturn(payment);

        Transaction transaction = stripePaymentService.deposit(RANDOM_WALLET, amount, RANDOM_CREDIT_CARD);

        assertEquals(amount, transaction.amount());
        assertEquals(DEPOSIT, transaction.type());
        assertEquals(payment.getId(), transaction.id());
    }

    @Test
    void refund_shouldReturnTransaction() {
        BigDecimal amount = BigDecimal.valueOf(100);
        Transaction originalTransaction = new Transaction(UUID.randomUUID().toString(), amount, DEPOSIT);
        Refund refund = new Refund(UUID.randomUUID().toString());
        when(stripeService.refund(any(String.class))).thenReturn(refund);

        Transaction transaction = stripePaymentService.refund(RANDOM_WALLET, originalTransaction);

        assertEquals(amount, transaction.amount());
        assertEquals(REFUND, transaction.type());
        assertEquals(refund.getId(), transaction.id());
    }

    private final static Wallet RANDOM_WALLET = new Wallet();
    private final static CreditCard RANDOM_CREDIT_CARD = new CreditCard("4242424242424242");
}