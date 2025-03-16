package com.playtomic.tests.wallet.infrastructure.repository;

import com.playtomic.tests.wallet.infrastructure.repository.entity.JpaWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaWalletRepository extends JpaRepository<JpaWalletEntity, UUID> {
    @Query("SELECT w FROM JpaWalletEntity w LEFT JOIN FETCH w.transactions WHERE w.id = :id")
    Optional<JpaWalletEntity> findByIdWithTransactions(@Param("id") UUID id);
}
