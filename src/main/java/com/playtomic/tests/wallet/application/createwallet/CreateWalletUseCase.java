package com.playtomic.tests.wallet.application.createwallet;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;

public class CreateWalletUseCase {

    private final WalletRepository walletRepository;

    public CreateWalletUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public CreateWalletResponse execute(CreateWalletRequest request) {
        var wallet = new Wallet(request.creditCardNumber());
        walletRepository.save(wallet);

        return new CreateWalletResponse.Success("Wallet created successfully");
    }
}
