package com.aquariux.test.controller;

import com.aquariux.test.dto.WalletBalanceResponse;
import com.aquariux.test.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/wallet")
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("{username}")
    public ResponseEntity<List<WalletBalanceResponse>> getUserWallets(@PathVariable String username) {
        List<WalletBalanceResponse> wallets = walletService.getUserWalletBalances(username);
        return ResponseEntity.ok(wallets);
    }
}
