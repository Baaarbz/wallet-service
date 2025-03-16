package com.playtomic.tests.wallet.application.getwallet;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetWalletUseCaseTest {

    private final WalletRepository walletRepository = mock(WalletRepository.class);
    private final GetWalletUseCase getWalletUseCase = new GetWalletUseCase(walletRepository);

    @Test
    void execute_WalletFound() {
        GetWalletRequest request = new GetWalletRequest(RANDOM_WALLET_ID);
        Wallet wallet = new Wallet("1234-5678-9012-3456");
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.of(wallet));

        GetWalletResponse response = getWalletUseCase.execute(request);

        assertResponse(response, wallet);
    }

    private static void assertResponse(GetWalletResponse response, Wallet wallet) {
        assertEquals(GetWalletResponse.Success.class, response.getClass());
        var successResponse = (GetWalletResponse.Success) response;
        assertEquals(wallet.id().value(), successResponse.getWalletId());
        assertEquals(wallet.balance().value(), successResponse.getBalance());
        assertEquals(wallet.associatedCreditCard().number(), successResponse.getAssociatedCreditCard());
        assertEquals(wallet.transactions().size(), successResponse.getTransactions().size());
    }

    @Test
    void execute_WalletNotFound() {
        GetWalletRequest request = new GetWalletRequest(RANDOM_WALLET_ID);
        when(walletRepository.findBy(any(WalletId.class))).thenReturn(Optional.empty());

        GetWalletResponse response = getWalletUseCase.execute(request);

        assertEquals(GetWalletResponse.NotFound.class, response.getClass());
        var notFoundResponse = (GetWalletResponse.NotFound) response;
        assertEquals("Wallet not found", notFoundResponse.getMessage());
    }

    private static final String RANDOM_WALLET_ID = UUID.randomUUID().toString();
}