package com.playtomic.tests.wallet.application.getwallet;

import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;

public class GetWalletUseCase {

    private final WalletRepository repository;

    public GetWalletUseCase(WalletRepository repository) {
        this.repository = repository;
    }

    public GetWalletResponse execute(GetWalletRequest request) {
        var wallet = repository.findBy(new WalletId(request.walletId()));

        if (wallet.isEmpty()) {
            return new GetWalletResponse.NotFound("Wallet not found");
        }

        return new GetWalletResponse.Success(wallet.get());
    }
}
