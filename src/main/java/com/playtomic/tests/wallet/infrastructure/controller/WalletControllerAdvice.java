package com.playtomic.tests.wallet.infrastructure.controller;


import com.playtomic.tests.wallet.infrastructure.stripe.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.infrastructure.stripe.exception.StripeServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WalletControllerAdvice {

    @ExceptionHandler(StripeAmountTooSmallException.class)
    public ResponseEntity<String> handleStripeAmountTooSmallException(StripeAmountTooSmallException ex) {
        return ResponseEntity.badRequest().body("The amount is too small to process.");
    }

    @ExceptionHandler(StripeServiceException.class)
    public ResponseEntity<String> handleStripeServiceException(StripeServiceException ex) {
        return ResponseEntity.badRequest().body("An error occurred while processing the payment.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleGenericException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}