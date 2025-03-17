package com.playtomic.tests.wallet.application.createwallet;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;

public class CreateWalletUseCase {

    private final WalletRepository walletRepository;

    public CreateWalletUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public CreateWalletResponse execute() {
        var wallet = new Wallet();
        walletRepository.save(wallet);

        return new CreateWalletResponse.Success(wallet.id().value());
    }
}
