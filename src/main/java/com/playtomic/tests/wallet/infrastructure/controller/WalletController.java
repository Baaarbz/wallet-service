package com.playtomic.tests.wallet.infrastructure.controller;

import com.playtomic.tests.wallet.application.buyproduct.BuyProductRequest;
import com.playtomic.tests.wallet.application.buyproduct.BuyProductResponse;
import com.playtomic.tests.wallet.application.buyproduct.BuyProductUseCase;
import com.playtomic.tests.wallet.application.createwallet.CreateWalletResponse;
import com.playtomic.tests.wallet.application.createwallet.CreateWalletUseCase;
import com.playtomic.tests.wallet.application.depositfounds.DepositFoundsRequest;
import com.playtomic.tests.wallet.application.depositfounds.DepositFoundsResponse;
import com.playtomic.tests.wallet.application.depositfounds.DepositFoundsUseCase;
import com.playtomic.tests.wallet.application.getwallet.GetWalletRequest;
import com.playtomic.tests.wallet.application.getwallet.GetWalletResponse;
import com.playtomic.tests.wallet.application.getwallet.GetWalletUseCase;
import com.playtomic.tests.wallet.application.refundpurchase.RefundPurchaseRequest;
import com.playtomic.tests.wallet.application.refundpurchase.RefundPurchaseResponse;
import com.playtomic.tests.wallet.application.refundpurchase.RefundPurchaseUseCase;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.infrastructure.controller.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/wallets")
public class WalletController {

    private final GetWalletUseCase getWalletUseCase;
    private final CreateWalletUseCase createWalletUseCase;
    private final DepositFoundsUseCase depositFoundsUseCase;
    private final BuyProductUseCase buyProductUseCase;
    private final RefundPurchaseUseCase refundPurchaseUseCase;

    public WalletController(GetWalletUseCase getWalletUseCase, CreateWalletUseCase createWalletUseCase, DepositFoundsUseCase depositFoundsUseCase, BuyProductUseCase buyProductUseCase, RefundPurchaseUseCase refundPurchaseUseCase) {
        this.getWalletUseCase = getWalletUseCase;
        this.createWalletUseCase = createWalletUseCase;
        this.depositFoundsUseCase = depositFoundsUseCase;
        this.buyProductUseCase = buyProductUseCase;
        this.refundPurchaseUseCase = refundPurchaseUseCase;
    }

    @GetMapping("{id}")
    public ResponseEntity<HttpGetWalletResponse> getWallet(@PathVariable String id) {
        var response = getWalletUseCase.execute(new GetWalletRequest(id));

        return switch (response) {
            case GetWalletResponse.Success success -> ResponseEntity.ok(mapToHttpResponse(success));
            case GetWalletResponse.NotFound notFound-> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    private HttpGetWalletResponse mapToHttpResponse(GetWalletResponse.Success success) {
        var transactions = success.getTransactions().stream()
                .map(transaction -> new HttpGetWalletResponse.Transaction(transaction.id(), transaction.amount(), transaction.type(), transaction.occurredOn().toString()))
                .toList();

        return new HttpGetWalletResponse(success.getWalletId(), success.getBalance(), transactions);
    }

    @PostMapping
    public ResponseEntity<HttpCreateWalletResponse> createWallet() {
        var response = createWalletUseCase.execute();

        return switch (response) {
            case CreateWalletResponse.Success success -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new HttpCreateWalletResponse(success.getWalletId()));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @PostMapping("/{id}/deposits")
    public ResponseEntity<Void> depositFunds(@PathVariable String id, @RequestBody HttpDepositFoundsToWalletRequest request) {
        var response = depositFoundsUseCase.execute(new DepositFoundsRequest(id, request.creditCardNumber(), request.amount()));

        return switch (response) {
            case DepositFoundsResponse.Success success -> ResponseEntity.ok().build();
            case DepositFoundsResponse.WalletNotFound notFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @PostMapping("/{id}/purchases")
    public ResponseEntity<Object> buyProduct(@PathVariable String id, @RequestBody HttpPurchaseProductRequest request) {
        var response = buyProductUseCase.execute(new BuyProductRequest(id, request.productPrice()));

        return switch (response) {
            case BuyProductResponse.Success success -> ResponseEntity.ok().build();
            case BuyProductResponse.NotFound notFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @PostMapping("/{id}/refunds")
    public ResponseEntity<Object> refundPurchase(@PathVariable String id, @RequestBody HttpRefundPurchaseToWalletRequest request) {
        var response = refundPurchaseUseCase.execute(new RefundPurchaseRequest(id, request.transactionId()));

        return switch (response) {
            case RefundPurchaseResponse.Success success -> ResponseEntity.ok().build();
            case RefundPurchaseResponse.WalletNotFound notFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
}
