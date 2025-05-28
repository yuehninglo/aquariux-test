package com.aquariux.test.service;

import com.aquariux.test.dto.WalletBalanceResponse;
import com.aquariux.test.entity.User;
import com.aquariux.test.entity.Wallet;
import com.aquariux.test.repository.UserRepository;
import com.aquariux.test.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository,
                         UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    public List<WalletBalanceResponse> getUserWalletBalances(String username) {
        User user = userRepository.findByUsername(username);
        List<Wallet> wallets = walletRepository.findByUser(user);
        return wallets.stream().map(w -> new WalletBalanceResponse(
                w.getCrypto().getSymbol(),
                w.getCrypto().getName(),
                w.getBalance()
        )).collect(Collectors.toList());
    }
}
