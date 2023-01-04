package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.model.Transaction;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
  BigInteger doTransaction(Transaction transaction);

  List<Transaction> findAllTransactions(final UUID clientID);

  List<Transaction> findAllTransactionsByWallet(final UUID clientID, final UUID walletID);
}
