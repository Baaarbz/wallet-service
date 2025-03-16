package com.playtomic.tests.wallet.infrastructure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/wallets")
public class WalletController {

    @GetMapping("{id}")
    public void getWallet(@PathVariable String id) {

    }
}
