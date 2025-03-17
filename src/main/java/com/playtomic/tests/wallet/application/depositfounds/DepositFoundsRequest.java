package com.playtomic.tests.wallet.application.depositfounds;

import java.math.BigDecimal;

public record DepositFoundsRequest(String walletId, String creditCardNumber, BigDecimal amount) {
}
