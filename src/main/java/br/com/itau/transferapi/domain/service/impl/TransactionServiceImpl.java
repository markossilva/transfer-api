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
import java.math.BigInteger;
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
    final BigDecimal transactionAmount = transaction.getAmount();
    final Transaction newTransaction = transactionRepository.save(transaction);

    synchronized (walletRepository) {
      final Wallet originWallet = getGetWallet(transaction, transaction.getOriginClientId(), transaction.getOriginWalletId());
      final Wallet targetWallet = getGetWallet(transaction, transaction.getTargetClientId(), transaction.getTargetWalletId());

      if (transactionAmount.compareTo(BigDecimal.valueOf(CLIENT_TRANSACTION_LIMIT)) >= ZERO_VALUE_COMPARATOR) {
        defineTransaction(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
        throw new TransactionDomainException(MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
      }

      if (originWallet.getBalance().compareTo(transactionAmount) <= ZERO_VALUE_COMPARATOR) {
        defineTransaction(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
        throw new TransactionDomainException(MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
      }

      updateWallet(originWallet, TransactionType.SEND, transactionAmount);
      updateWallet(targetWallet, TransactionType.RECEIVE, transactionAmount);

      defineTransaction(transaction, TransactionStatus.SUCCESS, MessageErrors.TRANSACTION_SUCCESS);
    }
    return newTransaction;
  }

  private void updateWallet(Wallet targetWallet, TransactionType type, BigDecimal transactionAmount) {
    targetWallet.setBalance(calculateBalance(type, targetWallet.getBalance(), transactionAmount));
    log.debug("Transaction type: {} \t amount: {} \t oldbalance: {}\tbalance: {}",
        type.name(), transactionAmount, targetWallet.getBalance(), targetWallet.getBalance());
    walletRepository.save(targetWallet);
  }

  private Wallet getGetWallet(final Transaction transaction, UUID clientId, UUID walletId) {
    return walletRepository.findById(clientId, walletId).orElseThrow(() -> {
      defineTransaction(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_OR_WALLET_NOT_EXISTS);
      return new TransactionDomainException(MessageErrors.CLIENT_OR_WALLET_NOT_EXISTS);
    });
  }

  private void defineTransaction(final Transaction transaction, TransactionStatus status, MessageErrors error) {
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
