package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.Balance;
import com.playtomic.tests.wallet.domain.valueobject.CreditCard;
import com.playtomic.tests.wallet.domain.valueobject.Transaction;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.playtomic.tests.wallet.domain.valueobject.TransactionType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WalletTest {

    @Test
    void deposit_withValidAmount_shouldIncreaseBalance() {
        PaymentService paymentService = Mockito.mock(PaymentService.class);
        Wallet wallet = new Wallet("1234 5678 9012 3456");
        BigDecimal depositAmount = BigDecimal.valueOf(20);
        Transaction depositTransaction = new Transaction(UUID.randomUUID().toString(), depositAmount, DEPOSIT);

        when(paymentService.deposit(any(Wallet.class), any(BigDecimal.class))).thenReturn(depositTransaction);

        Wallet updatedWallet = wallet.deposit(depositAmount, paymentService);

        assertEquals(depositAmount, updatedWallet.balance().value());
        assertEquals(1, updatedWallet.transactions().size());
        assertEquals(depositTransaction, updatedWallet.transactions().get(0));
        verify(paymentService).deposit(wallet, depositAmount);
    }

    @Test
    void deposit_withAmountLessThanMinimum_shouldThrowException() {
        PaymentService paymentService = Mockito.mock(PaymentService.class);
        Wallet wallet = new Wallet("1234 5678 9012 3456");
        BigDecimal depositAmount = BigDecimal.valueOf(5);

        assertThrows(IllegalArgumentException.class, () -> wallet.deposit(depositAmount, paymentService));
        verifyNoInteractions(paymentService);
    }

    @Test
    void refund_withValidTransaction_shouldIncreaseBalance() {
        PaymentService paymentService = Mockito.mock(PaymentService.class);
        Transaction buyTransaction = new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(-10), BUY);
        Wallet wallet = new Wallet(RANDOM_WALLET_ID, RANDOM_CREDIT_CARD_NUMBER, new Balance(BigDecimal.valueOf(10)), List.of(buyTransaction));
        Transaction refundTransaction = new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(10), REFUND);

        when(paymentService.refund(any(Wallet.class), any(Transaction.class))).thenReturn(refundTransaction);

        Wallet updatedWallet = wallet.refund(buyTransaction.id(), paymentService);

        assertEquals(BigDecimal.valueOf(20), updatedWallet.balance().value());
        assertEquals(2, updatedWallet.transactions().size());
        assertEquals(buyTransaction, updatedWallet.transactions().get(0));
        assertEquals(refundTransaction, updatedWallet.transactions().get(1));
        verify(paymentService).refund(wallet, buyTransaction);
    }

    @Test
    void refund_withNonExistentTransaction_shouldThrowException() {
        PaymentService paymentService = Mockito.mock(PaymentService.class);
        Wallet wallet = new Wallet("1234 5678 9012 3456");

        assertThrows(IllegalArgumentException.class, () -> wallet.refund(UUID.randomUUID().toString(), paymentService));
        verifyNoInteractions(paymentService);
    }

    @Test
    void refund_withNonBuyTransaction_shouldThrowException() {
        PaymentService paymentService = Mockito.mock(PaymentService.class);
        Transaction depositTransaction = new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(10), DEPOSIT);
        Wallet wallet = new Wallet(RANDOM_WALLET_ID, RANDOM_CREDIT_CARD_NUMBER, new Balance(BigDecimal.valueOf(10)), List.of(depositTransaction));

        assertThrows(IllegalArgumentException.class, () -> wallet.refund(depositTransaction.id(), paymentService));
        verifyNoInteractions(paymentService);
    }

    @Test
    void buy_withValidPrice_shouldDecreaseBalance() {
        Wallet wallet = new Wallet(RANDOM_WALLET_ID, RANDOM_CREDIT_CARD_NUMBER, new Balance(BigDecimal.valueOf(10)), List.of());
        BigDecimal price = BigDecimal.valueOf(10);

        Wallet updatedWallet = wallet.buy(price);

        assertEquals(BigDecimal.valueOf(0), updatedWallet.balance().value());
        assertEquals(1, updatedWallet.transactions().size());
        assertEquals(BUY, updatedWallet.transactions().get(0).type());
        assertEquals(price.negate(), updatedWallet.transactions().get(0).amount());
    }

    @Test
    void buy_withNegativePrice_shouldThrowException() {
        Wallet wallet = new Wallet("1234 5678 9012 3456");
        BigDecimal price = BigDecimal.valueOf(-10);

        assertThrows(IllegalArgumentException.class, () -> wallet.buy(price));
    }

    @Test
    void buy_withInsufficientFunds_shouldThrowException() {
        Wallet wallet = new Wallet(RANDOM_WALLET_ID, RANDOM_CREDIT_CARD_NUMBER, new Balance(BigDecimal.valueOf(10)), List.of());
        BigDecimal price = BigDecimal.valueOf(20);

        assertThrows(IllegalArgumentException.class, () -> wallet.buy(price));
    }

    @Test
    void buy_withNullPrice_shouldThrowException() {
        Wallet wallet = new Wallet("1234 5678 9012 3456");

        assertThrows(IllegalArgumentException.class, () -> wallet.buy(null));
    }

    private final static WalletId RANDOM_WALLET_ID = new WalletId(UUID.randomUUID().toString());
    private final static CreditCard RANDOM_CREDIT_CARD_NUMBER = new CreditCard("1234 5678 9012 3456");
}