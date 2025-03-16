package com.playtomic.tests.wallet.infrastructure.configuration;

import com.playtomic.tests.wallet.infrastructure.repository.JpaWalletRepository;
import com.playtomic.tests.wallet.infrastructure.repository.WalletDatasource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class RepositoryConfiguration {

    @Bean
    public WalletDatasource walletDatasource(JpaWalletRepository jpaWalletRepository) {
        return new WalletDatasource(jpaWalletRepository);
    }
}
