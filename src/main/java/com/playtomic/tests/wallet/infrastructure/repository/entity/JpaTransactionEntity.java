package com.playtomic.tests.wallet.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "transactions")
public class JpaTransactionEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private JpaWalletEntity wallet;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    private String type;

    @Column(name = "occurred_on")
    private LocalDateTime occurredOn;

    public JpaTransactionEntity() {
    }

    public JpaTransactionEntity(UUID id, BigDecimal amount, String type, LocalDateTime occurredOn) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.occurredOn = occurredOn;
    }
}