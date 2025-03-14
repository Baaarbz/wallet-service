package com.playtomic.tests.wallet.domain.service;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.valueobject.Transaction;

import java.math.BigDecimal;

public interface PaymentService {
    Transaction deposit(Wallet wallet, BigDecimal amount);

    Transaction refund(Wallet wallet, Transaction transaction);
}
