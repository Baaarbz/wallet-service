package com.playtomic.tests.wallet.infrastructure.configuration;

import com.playtomic.tests.wallet.infrastructure.stripe.StripePaymentService;
import com.playtomic.tests.wallet.infrastructure.stripe.StripeRestTemplateResponseErrorHandler;
import com.playtomic.tests.wallet.infrastructure.stripe.StripeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class StripeConfiguration {

    @Bean
    public StripeRestTemplateResponseErrorHandler stripeRestTemplateResponseErrorHandler() {
        return new StripeRestTemplateResponseErrorHandler();
    }

    @Bean
    public StripeService stripeService(@Value("${stripe.simulator.charges-uri}") URI chargesUri,
                                       @Value("${stripe.simulator.refunds-uri}") URI refundsUri,
                                       RestTemplateBuilder restTemplateBuilder) {
        return new StripeService(chargesUri, refundsUri, restTemplateBuilder);
    }

    @Bean
    public StripePaymentService stripePaymentService(StripeService stripeService) {
        return new StripePaymentService(stripeService);
    }
}
