package com.adriel.picpaydesafiobackend.transaction;

import org.springframework.stereotype.Service;

import com.adriel.picpaydesafiobackend.wallet.Wallet;
import com.adriel.picpaydesafiobackend.wallet.WalletRepository;
import com.adriel.picpaydesafiobackend.wallet.WalletType;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public Transaction create(Transaction transaction) {

        validade(transaction);

        var newTransaction = transactionRepository.save(transaction);

        var wallet = walletRepository.findById(transaction.payer()).get();
        walletRepository.save(wallet.debit(transaction.value()));

        return newTransaction;
    }

    private void validade(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                        .orElseThrow())
                .orElseThrow();
    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                payer.id().equals(transaction.payee());
    }
}
