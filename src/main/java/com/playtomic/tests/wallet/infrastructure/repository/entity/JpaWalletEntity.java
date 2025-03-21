package com.playtomic.tests.wallet.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "wallets")
public class JpaWalletEntity {

    @Id
    private UUID id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaTransactionEntity> transactions = new ArrayList<>();

    public JpaWalletEntity() {
    }

    public JpaWalletEntity(UUID id, BigDecimal balance, List<JpaTransactionEntity> transactions) {
        this.id = id;
        this.balance = balance;
        this.transactions = transactions;
    }
}