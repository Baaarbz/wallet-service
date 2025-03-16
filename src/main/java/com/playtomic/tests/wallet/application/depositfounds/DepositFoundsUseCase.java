package com.playtomic.tests.wallet.application.depositfounds;

import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.service.PaymentService;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;

public class DepositFoundsUseCase {

    private final WalletRepository walletRepository;
    private final PaymentService paymentService;

    public DepositFoundsUseCase(WalletRepository walletRepository, PaymentService paymentService) {
        this.walletRepository = walletRepository;
        this.paymentService = paymentService;
    }

    public DepositFoundsResponse execute(DepositFoundsRequest request) {
        var wallet = walletRepository.findBy(new WalletId(request.walletId()));
        if (wallet.isEmpty()) {
            return new DepositFoundsResponse.WalletNotFound("Wallet not found");
        }

        var walletFound = wallet.get();
        var walletUpdated = walletFound.deposit(request.amount(), paymentService);
        walletRepository.save(walletUpdated);

        return new DepositFoundsResponse.Success("Deposit successful");
    }
}
