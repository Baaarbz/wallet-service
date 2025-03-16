package com.playtomic.tests.wallet.infrastructure.stripe;


import com.playtomic.tests.wallet.infrastructure.WalletApplicationIT;
import com.playtomic.tests.wallet.infrastructure.stripe.dto.Payment;
import com.playtomic.tests.wallet.infrastructure.stripe.dto.Refund;
import com.playtomic.tests.wallet.infrastructure.stripe.exception.StripeAmountTooSmallException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test is failing with the current implementation.
 * <p>
 * How would you test this?
 */
public class StripeServiceTest extends WalletApplicationIT {

    @Autowired
    private StripeService stripeService;

    @Test
    public void refund_ok() {
        stubOkResponseForRefundRequest();

        var response = stripeService.refund("efa08f79-a937-4e7d-824f-cf823355a2b9");

        assertRefundRequestWasSent();
        assertEquals(new Refund("b85ce574-efdb-4f92-ba21-35460d18422a"), response);
    }

    private void stubOkResponseForRefundRequest() {
        stubFor(post(urlPathMatching("/payments/.*/refunds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"b85ce574-efdb-4f92-ba21-35460d18422a\",\"payment_id\":\"efa08f79-a937-4e7d-824f-cf823355a2b9\",\"amount\":10.10}")));
    }

    private void assertRefundRequestWasSent() {
        verify(postRequestedFor(urlEqualTo("/payments/efa08f79-a937-4e7d-824f-cf823355a2b9/refunds")));
    }

    @Test
    public void charge_ok() {
        stubOkResponseForChargeRequest();

        var response = stripeService.charge("4242 4242 4242 4242", new BigDecimal(15));

        assertChargeRequestWasSent();
        var expectedResponse = new Payment("bd4e65ad-9f43-4939-b176-0f428f46bea6");
        assertEquals(expectedResponse, response);
    }

    private void stubOkResponseForChargeRequest() {
        stubFor(post(urlPathMatching("/charges"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"bd4e65ad-9f43-4939-b176-0f428f46bea6\",\"amount\":15\n}")));
    }

    private void assertChargeRequestWasSent() {
        verify(postRequestedFor(urlEqualTo("/charges"))
                .withRequestBody(equalToJson("{\"credit_card\":\"4242 4242 4242 4242\",\"amount\":15}", true, false)));
    }

    @Test
    public void charge_exception() {
        stubFor(post(urlPathMatching("/charges")).willReturn(aResponse().withStatus(422)));

        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> stripeService.charge("4242 4242 4242 4242", new BigDecimal(5)));
    }
}
