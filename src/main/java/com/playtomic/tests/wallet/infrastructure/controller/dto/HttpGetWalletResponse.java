package com.playtomic.tests.wallet.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record HttpGetWalletResponse(String walletId, BigDecimal balance, List<Transaction> transactions) {
    public record Transaction(String id, BigDecimal amount, String type, String occurredOn) {
    }
}