package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.exception.MessageErrors;
import br.com.itau.transferapi.domain.exception.TransactionDomainException;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.TransactionStatus;
import br.com.itau.transferapi.domain.model.TransactionType;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
  private static final long CLIENT_TRANSACTION_LIMIT = 1000L;
  private final WalletRepository walletRepository;
  private final TransactionRepository transactionRepository;

  @Override
  public BigInteger doTransaction(Transaction transaction) {
    final BigDecimal transactionAmount = transaction.getAmount();
    final BigInteger transactionId = transactionRepository.save(transaction);

    synchronized (walletRepository) {
      final Wallet originWallet = getGetWallet(transaction, transaction.getOriginClientId(), transaction.getOriginWalletId());
      final Wallet targetWallet = getGetWallet(transaction, transaction.getTargetClientId(), transaction.getTargetWalletId());

      if (transactionAmount.compareTo(BigDecimal.valueOf(CLIENT_TRANSACTION_LIMIT)) == 0) {
        defineTransaction(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
        throw new TransactionDomainException(MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
      }

      if (originWallet.getBalance().compareTo(transactionAmount) <= 0) {
        defineTransaction(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
        throw new TransactionDomainException(MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
      }

      updateWallet(originWallet, TransactionType.SEND, transactionAmount);
      updateWallet(targetWallet, TransactionType.RECEIVE, transactionAmount);

      defineTransaction(transaction, TransactionStatus.SUCCESS, MessageErrors.TRANSACTION_SUCCESS);
    }
    return transactionId;
  }

  private void updateWallet(Wallet targetWallet, TransactionType receive, BigDecimal transactionAmount) {
    final Wallet wallet = targetWallet.toBuilder()
        .balance(calculateBalance(receive, targetWallet.getBalance(), transactionAmount))
        .build();
    log.debug("Transaction type: {} \t amount: {} \t oldbalance: {}\tbalance: {}", receive.name(), transactionAmount, targetWallet.getBalance(), wallet.getBalance());
    walletRepository.save(wallet);
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
    transactionRepository.update(updatedTransaction);
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
}
