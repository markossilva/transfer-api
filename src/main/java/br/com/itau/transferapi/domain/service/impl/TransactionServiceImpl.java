package br.com.itau.transferapi.domain.service.impl;

import br.com.itau.transferapi.domain.exception.ClientDomainException;
import br.com.itau.transferapi.domain.exception.MessageErrors;
import br.com.itau.transferapi.domain.exception.TransactionDomainException;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.TransactionStatus;
import br.com.itau.transferapi.domain.model.TransactionType;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
  private static final long CLIENT_TRANSACTION_LIMIT = 1000L;
  private static final int ZERO_VALUE_COMPARATOR = 0;
  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;

  @Override
  public Transaction doTransaction(Transaction transaction) {
    final Transaction initialTransaction = transactionRepository.save(transaction);

    synchronized (walletRepository) {
      final Wallet targetWallet = findAndVerifyWallet(initialTransaction,
          initialTransaction.getClientId(), initialTransaction.getWalletId());
      final Wallet originWallet = findAndVerifyWallet(initialTransaction,
          initialTransaction.getOriginClientId(), initialTransaction.getOriginWalletId());
      final BigDecimal transactionAmount = initialTransaction.getAmount();

      if (transactionAmount.compareTo(BigDecimal.valueOf(CLIENT_TRANSACTION_LIMIT)) >= ZERO_VALUE_COMPARATOR) {
        defineTransactionStatus(initialTransaction, TransactionStatus.FAIL, MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
        throw new TransactionDomainException(MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
      }

      if (originWallet.getBalance().compareTo(transactionAmount) <= ZERO_VALUE_COMPARATOR) {
        defineTransactionStatus(initialTransaction, TransactionStatus.FAIL, MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
        throw new TransactionDomainException(MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
      }

      updateWallet(originWallet, TransactionType.SEND, buildSendTransaction(initialTransaction, targetWallet));
      updateWallet(targetWallet, TransactionType.RECEIVE, initialTransaction);
    }

    return initialTransaction;
  }

  private Transaction buildSendTransaction(Transaction initTransaction, Wallet targetWallet) {
    return Transaction.builder()
        .originClientId(targetWallet.getClientId())
        .originWalletId(targetWallet.getId())
        .walletId(initTransaction.getOriginWalletId())
        .clientId(initTransaction.getOriginClientId())
        .amount(initTransaction.getAmount())
        .date(LocalDateTime.now())
        .build();
  }

  private void updateWallet(Wallet wallet, TransactionType type, Transaction transaction) {

    wallet.setBalance(calculateBalance(type, wallet.getBalance(), transaction.getAmount()));
    walletRepository.save(wallet);

    transaction.setType(type);
    defineTransactionStatus(transaction, TransactionStatus.SUCCESS, MessageErrors.TRANSACTION_SUCCESS);
  }

  private Wallet findAndVerifyWallet(final Transaction transaction, UUID clientId, UUID walletId) {
    return walletRepository.findById(clientId, walletId).orElseThrow(() -> {
      defineTransactionStatus(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_OR_WALLET_NOT_EXISTS);
      return new TransactionDomainException(MessageErrors.CLIENT_OR_WALLET_NOT_EXISTS);
    });
  }

  private void defineTransactionStatus(final Transaction transaction, TransactionStatus status, MessageErrors error) {
    final Transaction updatedTransaction = transaction.toBuilder()
        .status(status)
        .cause(error.getMessage())
        .date(LocalDateTime.now())
        .build();
    transactionRepository.save(updatedTransaction);
  }

  private BigDecimal calculateBalance(TransactionType type, BigDecimal balance, BigDecimal amount) {
    BigDecimal newBalance;
    switch (type) {
      case SEND:
        newBalance = balance.subtract(amount);
        break;
      case RECEIVE:
        newBalance = balance.add(amount);
        break;
      default:
        throw new TransactionDomainException(MessageErrors.UNKNOWN_TRANSACTION_TYPE);
    }

    return newBalance;
  }

  @Override
  public List<Transaction> findAllTransactions(UUID clientId) {
    return transactionRepository.findAllByClientId(clientId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_HAS_NO_TRANSACTIONS));
  }

  @Override
  public Transaction findAllTransactionsByWallet(UUID clientId, UUID walletId) {
    return transactionRepository.findAllByClientIdAndWallet(clientId, walletId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_HAS_NO_TRANSACTIONS));
  }
}
