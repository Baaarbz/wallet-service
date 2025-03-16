package com.playtomic.tests.wallet.application.buyproduct;

import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;

public class BuyProductUseCase {

    private final WalletRepository walletRepository;

    public BuyProductUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public BuyProductResponse execute(BuyProductRequest request) {
        var wallet = walletRepository.findBy(new WalletId(request.walletId()));
        if (wallet.isEmpty()) {
            return new BuyProductResponse.NotFound("Wallet not found with id: " + request.walletId());
        }
        var updatedWallet = wallet.get().buy(request.price());
        walletRepository.save(updatedWallet);

        return new BuyProductResponse.Success("Product bought successfully");
    }
}
