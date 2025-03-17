package com.playtomic.tests.wallet.application.createwallet;


import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateWalletUseCaseTest {

    private final WalletRepository walletRepository = mock();
    private final CreateWalletUseCase createWalletUseCase = new CreateWalletUseCase(walletRepository);

    @Test
    void execute_WalletCreatedSuccessfully() {
        doNothing().when(walletRepository).save(any(Wallet.class));

        CreateWalletResponse response = createWalletUseCase.execute();

        verify(walletRepository).save(any());
        assertEquals(CreateWalletResponse.Success.class, response.getClass());
        CreateWalletResponse.Success successResponse = (CreateWalletResponse.Success) response;
        assertNotNull(successResponse.getWalletId());
    }
}