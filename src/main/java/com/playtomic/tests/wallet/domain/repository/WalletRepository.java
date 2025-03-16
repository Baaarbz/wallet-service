package com.playtomic.tests.wallet.domain.repository;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.valueobject.WalletId;

import java.util.Optional;

public interface WalletRepository {
    void save(Wallet wallet);
    Optional<Wallet> findBy(WalletId id);
}
