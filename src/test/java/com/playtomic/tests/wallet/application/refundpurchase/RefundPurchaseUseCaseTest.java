package com.playtomic.tests.wallet.application.refundpurchase;

import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;
import com.playtomic.tests.wallet.domain.Wallet;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class RefundPurchaseUseCaseTest {

    private final PaymentService paymentService = mock();
    private final WalletRepository walletRepository = mock();
    private final RefundPurchaseUseCase refundPurchaseUseCase = new RefundPurchaseUseCase(walletRepository, paymentService);

    @Test
    void execute_RefundSuccessful() {
        RefundPurchaseRequest request = new RefundPurchaseRequest(RANDOM_WALLET_ID, "transaction-id");
        Wallet wallet = mock(Wallet.class);
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.of(wallet));
        when(wallet.refund(anyString(), eq(paymentService))).thenReturn(wallet);

        RefundPurchaseResponse response = refundPurchaseUseCase.execute(request);

        verify(walletRepository).save(wallet);
        assertEquals("Transaction refunded successfully", response.getMessage());
        assertEquals(RefundPurchaseResponse.Success.class, response.getClass());
    }

    @Test
    void execute_WalletNotFound() {
        RefundPurchaseRequest request = new RefundPurchaseRequest(RANDOM_WALLET_ID, "transaction-id");
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.empty());

        RefundPurchaseResponse response = refundPurchaseUseCase.execute(request);

        assertEquals("Wallet not found", response.getMessage());
        assertEquals(RefundPurchaseResponse.WalletNotFound.class, response.getClass());
    }

    private static final String RANDOM_WALLET_ID = UUID.randomUUID().toString();
}