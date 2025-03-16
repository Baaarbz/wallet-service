package com.playtomic.tests.wallet.infrastructure.repository;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.valueobject.Balance;
import com.playtomic.tests.wallet.domain.valueobject.CreditCard;
import com.playtomic.tests.wallet.domain.valueobject.Transaction;
import com.playtomic.tests.wallet.domain.valueobject.TransactionType;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;
import com.playtomic.tests.wallet.infrastructure.repository.entity.JpaTransactionEntity;
import com.playtomic.tests.wallet.infrastructure.repository.entity.JpaWalletEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class WalletDatasource implements WalletRepository {

    private final JpaWalletRepository jpaWalletRepository;

    public WalletDatasource(JpaWalletRepository jpaWalletRepository) {
        this.jpaWalletRepository = jpaWalletRepository;
    }

    @Override
    public void save(Wallet wallet) {
        List<JpaTransactionEntity> transactionEntities = wallet.transactions().stream()
                .map(this::mapToJpaTransactionEntity)
                .collect(Collectors.toList());

        JpaWalletEntity walletEntity = new JpaWalletEntity(
                UUID.fromString(wallet.id().value()),
                wallet.associatedCreditCard().number(),
                wallet.balance().value(),
                transactionEntities
        );

        for (JpaTransactionEntity transaction : transactionEntities) {
            transaction.setWallet(walletEntity);
        }
        jpaWalletRepository.save(walletEntity);
    }

    @Override
    public Optional<Wallet> findBy(WalletId id) {
        return jpaWalletRepository.findByIdWithTransactions(UUID.fromString(id.value()))
                .map(this::mapToDomainWallet);
    }

    private JpaTransactionEntity mapToJpaTransactionEntity(Transaction transaction) {
        return new JpaTransactionEntity(
                UUID.fromString(transaction.id()),
                transaction.amount(),
                transaction.type().name(),
                transaction.occurredOn()
        );
    }

    private Wallet mapToDomainWallet(JpaWalletEntity entity) {
        List<Transaction> transactions = entity.getTransactions().stream()
                .map(this::mapToDomainTransaction)
                .collect(Collectors.toList());

        return new Wallet(
                new WalletId(entity.getId().toString()),
                new CreditCard(entity.getCreditCardNumber()),
                new Balance(entity.getBalance()),
                transactions
        );
    }

    private Transaction mapToDomainTransaction(JpaTransactionEntity entity) {
        return new Transaction(
                entity.getId().toString(),
                entity.getAmount(),
                TransactionType.valueOf(entity.getType()),
                entity.getOccurredOn()
        );
    }
}