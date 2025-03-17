package com.playtomic.tests.wallet.infrastructure.controller.dto;

import java.math.BigDecimal;

public record HttpPurchaseProductRequest(BigDecimal productPrice) {
}
