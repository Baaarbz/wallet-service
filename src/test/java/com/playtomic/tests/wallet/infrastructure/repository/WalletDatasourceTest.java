package com.playtomic.tests.wallet.infrastructure.repository;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.valueobject.*;
import com.playtomic.tests.wallet.infrastructure.WalletApplicationIT;
import com.playtomic.tests.wallet.infrastructure.repository.entity.JpaWalletEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class WalletDatasourceTest extends WalletApplicationIT {

    @Autowired
    private WalletDatasource walletDatasource;

    @Autowired
    private JpaWalletRepository jpaWalletRepository;

    @Test
    void saveWallet_shouldPersistWalletAndTransactions() {
        var wallet = givenAWalletWithTransactions();

        walletDatasource.save(wallet);

        assertWalletWasSavedCorrectlyInDatabase(wallet);
    }

    private void assertWalletWasSavedCorrectlyInDatabase(Wallet originalWallet) {
        Optional<JpaWalletEntity> result = jpaWalletRepository.findByIdWithTransactions(UUID.fromString(originalWallet.id().value()));
        assertThat(result).isPresent();
        JpaWalletEntity persistedWallet = result.get();

        assertThat(persistedWallet.getId().toString()).isEqualTo(originalWallet.id().value());
        assertThat(persistedWallet.getCreditCardNumber()).isEqualTo(originalWallet.associatedCreditCard().number());
        assertThat(persistedWallet.getBalance()).isEqualByComparingTo(originalWallet.balance().value());

        assertThat(persistedWallet.getTransactions()).hasSize(2);
        assertThat(persistedWallet.getTransactions()).allMatch(tx -> tx.getWallet().equals(persistedWallet));
    }

    @Test
    void findByWalletId_whenWalletExists_shouldReturnWallet() {
        var walletInDatabase = givenAWalletWithTransactionsInDatabase();

        Optional<Wallet> result = walletDatasource.findBy(walletInDatabase.id());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(walletInDatabase);
    }

    @Test
    void findByWalletId_whenWalletDoesNotExist_shouldReturnEmpty() {
        Optional<Wallet> result = walletDatasource.findBy(new WalletId(UUID.randomUUID().toString()));

        assertThat(result).isEmpty();
    }

    private Wallet givenAWalletWithTransactions() {
        String walletId = UUID.randomUUID().toString();
        String creditCardNumber = "4242424242424242";
        BigDecimal balance = new BigDecimal("100.00");

        Transaction transaction1 = new Transaction(
                UUID.randomUUID().toString(),
                new BigDecimal("50.00"),
                TransactionType.DEPOSIT,
                LocalDateTime.now().minusDays(1)
        );

        Transaction transaction2 = new Transaction(
                UUID.randomUUID().toString(),
                new BigDecimal("-20.00"),
                TransactionType.BUY,
                LocalDateTime.now()
        );

        return new Wallet(
                new WalletId(walletId),
                new CreditCard(creditCardNumber),
                new Balance(balance),
                List.of(transaction1, transaction2)
        );
    }

    private Wallet givenAWalletWithTransactionsInDatabase() {
        var wallet = givenAWalletWithTransactions();

        walletDatasource.save(wallet);

        return wallet;
    }
}