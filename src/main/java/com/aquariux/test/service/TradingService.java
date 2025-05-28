package com.aquariux.test.service;

import com.aquariux.test.dto.TradeRequest;
import com.aquariux.test.dto.TradeResponse;
import com.aquariux.test.dto.WalletBalanceResponse;
import com.aquariux.test.entity.*;
import com.aquariux.test.enums.Crypto;
import com.aquariux.test.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TradingService {

    private final TransactionRepository transactionRepository;
    private final TradingPairRepository tradingPairRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    public TradingService(TransactionRepository transactionRepository,
                          TradingPairRepository tradingPairRepository,
                          WalletRepository walletRepository,
                          UserRepository userRepository,
                          PriceSnapshotRepository priceSnapshotRepository,
                          CryptocurrencyRepository cryptocurrencyRepository) {
        this.transactionRepository = transactionRepository;
        this.tradingPairRepository = tradingPairRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.priceSnapshotRepository = priceSnapshotRepository;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    @Transactional
    public TradeResponse executeTrade(String username, TradeRequest request) throws Exception {
        User user = userRepository.findByUsername(username);
        log.debug("user info: {}", user);
        String cryptoSymbol = request.getCryptoSymbol();
        TradingPair pair =
                tradingPairRepository.findBySymbolIgnoreCase(request.getTradingSymbol()).get();
        Transaction.Side side = Transaction.Side.valueOf(request.getSide());
        PriceSnapshot snap = priceSnapshotRepository.findByTradingPairId(pair.getId()).get();
        BigDecimal exePrice = side == Transaction.Side.BUY? snap.getAskPrice(): snap.getBidPrice();

        if (hasEnoughBalance(user, exePrice, request.getAmount(), side, cryptoSymbol)) {

            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setTradingPair(pair);
            transaction.setSide(Transaction.Side.valueOf(request.getSide()));
            BigDecimal amount = request.getAmount();
            transaction.setAmount(amount);
            transaction.setPrice(exePrice);
            BigDecimal total = exePrice.multiply(amount);
            transaction.setTotal(total);

            Transaction savedTransaction = transactionRepository.save(transaction);
            calcNewBalance(user, amount, total, side, cryptoSymbol);

            return new TradeResponse(
                    savedTransaction.getId(),
                    savedTransaction.getTradingPair().getSymbol(),
                    savedTransaction.getSide().name(),
                    savedTransaction.getAmount(),
                    savedTransaction.getPrice(),
                    savedTransaction.getTotal(),
                    savedTransaction.getCreatedAt()
            );
        } else {
            throw new Exception("Not enough Crypto for buy/sell");
        }
    }

    private void calcNewBalance(User user,
                                BigDecimal amount,
                                BigDecimal total,
                                Transaction.Side side,
                                String cryptoSymbol) {
        log.info("Calculate new balance for user: {}", user);
        Cryptocurrency crypto = cryptocurrencyRepository.findBySymbol(cryptoSymbol);
        Cryptocurrency USDT = cryptocurrencyRepository.findBySymbol(Crypto.USDT.name());
        BigDecimal currentCryptoAmount
                = walletRepository.findByUser(user).stream()
                .filter(e -> e.getCrypto().getSymbol().equals(cryptoSymbol))
                .toList().get(0).getBalance();
        BigDecimal currentUSDTBalance = walletRepository.findByUser(user)
                .stream().filter(e -> e.getCrypto().getSymbol().equals(Crypto.USDT.name()))
                .toList().get(0).getBalance();
        BigDecimal newCryptoAmount;
        BigDecimal newUSDTBalance;
        if (side == Transaction.Side.BUY) {
            newCryptoAmount = currentCryptoAmount.add(amount);
            newUSDTBalance = currentUSDTBalance.subtract(total);
        } else {
            newCryptoAmount = currentCryptoAmount.subtract(amount);
            newUSDTBalance = currentUSDTBalance.add(total);
        }
        walletRepository.updateByUserAndCrypto(user, crypto, newCryptoAmount);
        walletRepository.updateByUserAndCrypto(user, USDT, newUSDTBalance);
    }

    private boolean hasEnoughBalance(User user,
                                     BigDecimal exePrice,
                                     BigDecimal amount,
                                     Transaction.Side side,
                                     String cryptoSymbol) {
        if (side == Transaction.Side.BUY) {
            BigDecimal USDTBalance = walletRepository.findByUser(user)
                    .stream().filter(e -> e.getCrypto().getSymbol().equals(Crypto.USDT.name()))
                    .toList().get(0).getBalance();
            BigDecimal exeTotal = exePrice.multiply(amount);
            return USDTBalance.compareTo(exeTotal) == 1;
        } else {
            BigDecimal cryptoBalance = walletRepository.findByUser(user)
                    .stream().filter(e -> e.getCrypto().getSymbol().equals(cryptoSymbol))
                    .toList().get(0).getBalance();
            return cryptoBalance.compareTo(amount) == 1;
        }
    }

    @Transactional(readOnly = true)
    public List<TradeResponse> getUserTransactions(User user) {
        List<Transaction> transactions
                = transactionRepository.findByUserOrderByCreatedAtDesc(user);
        return transactions.stream().map(t -> new TradeResponse(
                        t.getId(),
                        t.getTradingPair().getSymbol(),
                        t.getSide().name(),
                        t.getAmount(),
                        t.getPrice(),
                        t.getTotal(),
                        t.getCreatedAt()
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WalletBalanceResponse> getUserWalletBalances(User user) {
        List<Wallet> wallets = walletRepository.findByUser(user);
        return wallets.stream().map(w -> new WalletBalanceResponse(
                        w.getCrypto().getSymbol(),
                        w.getCrypto().getName(),
                        w.getBalance()
                )).collect(Collectors.toList());
    }
}
