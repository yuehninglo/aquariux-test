package com.aquariux.test.service;

import com.aquariux.test.dto.TradeRequest;
import com.aquariux.test.dto.TradeResponse;
import com.aquariux.test.dto.WalletBalanceResponse;
import com.aquariux.test.entity.*;
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

    @Autowired
    public TradingService(TransactionRepository transactionRepository,
                          TradingPairRepository tradingPairRepository,
                          WalletRepository walletRepository,
                          UserRepository userRepository,
                          PriceSnapshotRepository priceSnapshotRepository) {
        this.transactionRepository = transactionRepository;
        this.tradingPairRepository = tradingPairRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.priceSnapshotRepository = priceSnapshotRepository;
    }

    @Transactional
    public TradeResponse executeTrade(Long userId, TradeRequest request) {
        User user = userRepository.getReferenceById(userId);
        TradingPair pair =
                tradingPairRepository.findBySymbolIgnoreCase(request.getTradingSymbol()).get();
        Transaction.Side side = Transaction.Side.valueOf(request.getSide());

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTradingPair(pair);
        transaction.setSide(Transaction.Side.valueOf(request.getSide()));
        BigDecimal amount = request.getAmount();
        transaction.setAmount(amount);
        BigDecimal bestPrice = getPrice(side, pair);
        transaction.setPrice(bestPrice);
        BigDecimal total = bestPrice.multiply(amount);
        transaction.setTotal(total);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TradeResponse(
                savedTransaction.getId(),
                savedTransaction.getTradingPair().getSymbol(),
                savedTransaction.getSide().name(),
                savedTransaction.getAmount(),
                savedTransaction.getPrice(),
                savedTransaction.getTotal(),
                savedTransaction.getCreatedAt()
        );
    }

    private BigDecimal getPrice(Transaction.Side side, TradingPair pair) {
        PriceSnapshot snap = priceSnapshotRepository.findByTradingPairId(pair.getId()).get();
        if (side == Transaction.Side.BUY) return snap.getAskPrice();
        else return snap.getBidPrice();
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
