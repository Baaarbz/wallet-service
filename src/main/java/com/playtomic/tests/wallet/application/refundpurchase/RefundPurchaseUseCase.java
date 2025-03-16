package com.playtomic.tests.wallet.application.refundpurchase;

import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;

public class RefundPurchaseUseCase {

    private final WalletRepository walletRepository;
    private final PaymentService paymentService;

    public RefundPurchaseUseCase(WalletRepository walletRepository, PaymentService paymentService) {
        this.walletRepository = walletRepository;
        this.paymentService = paymentService;
    }

    public RefundPurchaseResponse execute(RefundPurchaseRequest request) {
        var wallet = walletRepository.findBy(new WalletId(request.walletId()));

        if (wallet.isEmpty()) {
            return new RefundPurchaseResponse.WalletNotFound("Wallet not found");
        }

        var walletUpdated = wallet.get().refund(request.transactionId(), paymentService);
        walletRepository.save(walletUpdated);

        return new RefundPurchaseResponse.Success("Transaction refunded successfully");
    }
}
