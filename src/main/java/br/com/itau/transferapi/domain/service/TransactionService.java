package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.WalletTransactions;

import java.util.List;
import java.util.UUID;

/**
 * Transaction service, provide all client transactions success or fail
 *
 * @author markos
 */
public interface TransactionService {

  /**
   * Create an action of transaction to transfer balance between wallets,
   * with status {@link br.com.itau.transferapi.domain.model.TransactionStatus#PROCESSING}
   * and type {@link br.com.itau.transferapi.domain.model.TransactionType#SEND}
   *
   * @param transaction a representation values of the transaction
   * @return a created transaction with {@link Transaction}
   */
  Transaction doTransaction(Transaction transaction);

  /**
   * Find all client transactions by identifier
   *
   * @param clientID client identifier
   * @return all client {@link Transaction}
   * @throws br.com.itau.transferapi.domain.exception.TransactionDomainException when not found any transaction
   */
  List<Transaction> findAllTransactions(final UUID clientID);

  /**
   * Find all client transactions with client and wallet identifier
   *
   * @param clientID client identifier
   * @param walletID wallet identifier
   * @return a list of client {@link Transaction} by client and wallet identifier
   * @throws br.com.itau.transferapi.domain.exception.TransactionDomainException when not found any wallet transaction
   */
  List<Transaction> findAllTransactionsByWallet(final UUID clientID, final UUID walletID);

  /**
   * Find all transactions by wallet identifier
   *
   * @param walletID wallet identifier
   * @return a list of specific {@link WalletTransactions} by wallet identifier
   * @throws br.com.itau.transferapi.domain.exception.TransactionDomainException
   */
  List<WalletTransactions> findAllByWalletId(UUID walletID);
}
