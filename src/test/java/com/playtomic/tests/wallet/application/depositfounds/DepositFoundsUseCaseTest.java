package com.playtomic.tests.wallet.application.depositfounds;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepositFoundsUseCaseTest {

    private final WalletRepository walletRepository = mock();
    private final PaymentService paymentService = mock();
    private final DepositFoundsUseCase depositFoundsUseCase = new DepositFoundsUseCase(walletRepository, paymentService);

    @Test
    void execute_DepositSuccessful() {
        DepositFoundsRequest request = new DepositFoundsRequest(RANDOM_WALLET_ID, BigDecimal.valueOf(100.0));
        Wallet wallet = mock();
        Wallet updatedWallet = mock();
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.of(wallet));
        when(wallet.deposit(any(), eq(paymentService))).thenReturn(updatedWallet);

        DepositFoundsResponse response = depositFoundsUseCase.execute(request);

        verify(walletRepository).save(updatedWallet);
        assertEquals("Deposit successful", response.getMessage());
        assertEquals(DepositFoundsResponse.Success.class, response.getClass());
    }

    @Test
    void execute_WalletNotFound() {
        DepositFoundsRequest request = new DepositFoundsRequest(RANDOM_WALLET_ID, BigDecimal.valueOf(100.0));
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.empty());

        DepositFoundsResponse response = depositFoundsUseCase.execute(request);

        verify(walletRepository, never()).save(any());
        assertEquals("Wallet not found", response.getMessage());
        assertEquals(DepositFoundsResponse.WalletNotFound.class, response.getClass());
    }

    private static final String RANDOM_WALLET_ID = UUID.randomUUID().toString();
}