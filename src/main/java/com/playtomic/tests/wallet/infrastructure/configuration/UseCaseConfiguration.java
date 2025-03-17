package com.playtomic.tests.wallet.infrastructure.configuration;

import com.playtomic.tests.wallet.application.buyproduct.BuyProductUseCase;
import com.playtomic.tests.wallet.application.createwallet.CreateWalletUseCase;
import com.playtomic.tests.wallet.application.depositfounds.DepositFoundsUseCase;
import com.playtomic.tests.wallet.application.getwallet.GetWalletUseCase;
import com.playtomic.tests.wallet.application.refundpurchase.RefundPurchaseUseCase;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public BuyProductUseCase buyProductUseCase(WalletRepository walletRepository) {
        return new BuyProductUseCase(walletRepository);
    }

    @Bean
    public RefundPurchaseUseCase refundPurchaseUseCase(WalletRepository walletRepository) {
        return new RefundPurchaseUseCase(walletRepository);
    }

    @Bean
    public CreateWalletUseCase createWalletUseCase(WalletRepository walletRepository) {
        return new CreateWalletUseCase(walletRepository);
    }

    @Bean
    public DepositFoundsUseCase depositFoundsUseCase(WalletRepository walletRepository, PaymentService paymentService) {
        return new DepositFoundsUseCase(walletRepository, paymentService);
    }

    @Bean
    public GetWalletUseCase getWalletUseCase(WalletRepository walletRepository) {
        return new GetWalletUseCase(walletRepository);
    }
}
