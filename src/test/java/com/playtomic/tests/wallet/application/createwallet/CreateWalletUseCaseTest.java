package com.playtomic.tests.wallet.application.createwallet;


import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateWalletUseCaseTest {

    private final WalletRepository walletRepository = mock();
    private final CreateWalletUseCase createWalletUseCase = new CreateWalletUseCase(walletRepository);

    @Test
    void execute_WalletCreatedSuccessfully() {
        CreateWalletRequest request = new CreateWalletRequest("1234-5678-9012-3456");
        doNothing().when(walletRepository).save(any(Wallet.class));

        CreateWalletResponse response = createWalletUseCase.execute(request);

        verify(walletRepository).save(any());
        assertEquals("Wallet created successfully", response.getMessage());
        assertEquals(CreateWalletResponse.Success.class, response.getClass());
    }
}