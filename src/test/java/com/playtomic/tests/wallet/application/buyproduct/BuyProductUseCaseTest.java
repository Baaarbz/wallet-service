package com.playtomic.tests.wallet.application.buyproduct;


import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class BuyProductUseCaseTest {
    private final WalletRepository walletRepository = mock();
    private final BuyProductUseCase buyProductUseCase = new BuyProductUseCase(walletRepository);

    @Test
    void execute_WalletNotFound() {
        BuyProductRequest request = new BuyProductRequest(RANDOM_WALLET_ID, BigDecimal.valueOf(100.0));
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.empty());

        BuyProductResponse response = buyProductUseCase.execute(request);

        assertEquals("Wallet not found with id: " + RANDOM_WALLET_ID, response.getMessage());
        assertEquals(BuyProductResponse.NotFound.class, response.getClass());
        verify(walletRepository).findBy(new WalletId(RANDOM_WALLET_ID));
        verifyNoMoreInteractions(walletRepository);
    }

    @Test
    void execute_Success() {
        BuyProductRequest request = new BuyProductRequest(RANDOM_WALLET_ID, BigDecimal.valueOf(100.0));
        Wallet wallet = mock(Wallet.class);
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.of(wallet));
        when(wallet.buy(any(BigDecimal.class))).thenReturn(wallet);

        BuyProductResponse response = buyProductUseCase.execute(request);

        verify(walletRepository).save(wallet);
        assertEquals("Product bought successfully", response.getMessage());
        assertEquals(BuyProductResponse.Success.class, response.getClass());
    }

    private static final String RANDOM_WALLET_ID = UUID.randomUUID().toString();
}