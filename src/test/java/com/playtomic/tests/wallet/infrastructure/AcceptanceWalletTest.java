package com.playtomic.tests.wallet.infrastructure;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class AcceptanceWalletTest extends WalletApplicationIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1";
    }

    @Test
    void completeWalletFlow_happyPath() {
        // Step 1: Create a wallet
        String walletId = createWallet();

        // Step 2: Deposit funds
        stubOkResponseForChargeRequest();
        depositFunds(walletId, "4242424242424242", new BigDecimal("100.00"));

        // Step 3: Make a purchase
        makePurchase(walletId, new BigDecimal("25.00"));

        // Step 4: Get wallet details to retrieve purchase transaction ID
        Response walletResponse = getWallet(walletId);
        List<Map<String, Object>> transactions = walletResponse.jsonPath().getList("transactions");
        String purchaseTransactionId = findPurchaseTransaction(transactions);

        // Step 5: Refund the purchase
        refundPurchase(walletId, purchaseTransactionId);

        // Step 6: Get final wallet state and verify
        Response finalWalletResponse = getWallet(walletId);

        // Verify wallet balance
        assertThat(finalWalletResponse.jsonPath().getDouble("balance")).isEqualTo(100.00);

        // Verify transactions
        List<Map<String, Object>> finalTransactions = finalWalletResponse.jsonPath().getList("transactions");
        assertThat(finalTransactions).hasSize(3);

        // Verify deposit transaction
        Map<String, Object> depositTx = findTransactionByType(finalTransactions, "DEPOSIT");
        assertThat(depositTx.get("amount")).isEqualTo(100.0f);

        // Verify purchase transaction
        Map<String, Object> purchaseTx = findTransactionByType(finalTransactions, "BUY");
        assertThat(purchaseTx.get("amount")).isEqualTo(-25.0f);

        // Verify refund transaction
        Map<String, Object> refundTx = findTransactionByType(finalTransactions, "REFUND");
        assertThat(refundTx.get("amount")).isEqualTo(25.0f);
    }

    private String createWallet() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .post("/wallets")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getString("walletId");
    }

    private void stubOkResponseForChargeRequest() {
        stubFor(post(urlPathMatching("/charges"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"bd4e65ad-9f43-4939-b176-0f428f46bea6\",\"amount\":15\n}")));
    }

    private void depositFunds(String walletId, String creditCardNumber, BigDecimal amount) {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "creditCardNumber", creditCardNumber,
                        "amount", amount
                ))
                .when()
                .post("/wallets/{id}/deposits", walletId)
                .then()
                .statusCode(200);
    }

    private void makePurchase(String walletId, BigDecimal price) {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("productPrice", price))
                .when()
                .post("/wallets/{id}/purchases", walletId)
                .then()
                .statusCode(200);
    }

    private Response getWallet(String walletId) {
        return given()
                .when()
                .get("/wallets/{id}", walletId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    private void refundPurchase(String walletId, String transactionId) {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("transactionId", transactionId))
                .when()
                .post("/wallets/{id}/refunds", walletId)
                .then()
                .statusCode(200);
    }

    private String findPurchaseTransaction(List<Map<String, Object>> transactions) {
        return transactions.stream()
                .filter(tx -> tx.get("type").equals("BUY"))
                .findFirst()
                .map(tx -> tx.get("id").toString())
                .orElseThrow(() -> new AssertionError("Purchase transaction not found"));
    }

    private Map<String, Object> findTransactionByType(List<Map<String, Object>> transactions, String type) {
        return transactions.stream()
                .filter(tx -> tx.get("type").equals(type))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Transaction of type " + type + " not found"));
    }
}