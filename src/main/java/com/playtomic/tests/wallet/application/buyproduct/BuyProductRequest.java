package com.playtomic.tests.wallet.application.buyproduct;

import java.math.BigDecimal;

public record BuyProductRequest(String walletId, BigDecimal price) {}
