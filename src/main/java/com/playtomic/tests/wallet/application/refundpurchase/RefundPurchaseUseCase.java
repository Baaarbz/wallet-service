package com.playtomic.tests.wallet.application.refundpurchase;

import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;

public class RefundPurchaseUseCase {

    private final WalletRepository walletRepository;

    public RefundPurchaseUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public RefundPurchaseResponse execute(RefundPurchaseRequest request) {
        var wallet = walletRepository.findBy(new WalletId(request.walletId()));

        if (wallet.isEmpty()) {
            return new RefundPurchaseResponse.WalletNotFound("Wallet not found");
        }

        var walletFound = wallet.get();
        var walletUpdated = walletFound.refund(request.transactionId());
        walletRepository.save(walletUpdated);

        return new RefundPurchaseResponse.Success("Transaction refunded successfully");
    }
}
