package com.playtomic.tests.wallet.infrastructure.controller.dto;

import java.math.BigDecimal;

public record HttpDepositFoundsToWalletRequest(String creditCardNumber, BigDecimal amount) {
}
