package br.com.itau.transferapi.domain.service.impl;

import br.com.itau.transferapi.domain.exception.MessageErrors;
import br.com.itau.transferapi.domain.exception.TransactionDomainException;
import br.com.itau.transferapi.domain.model.*;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class TransactionEventListener implements ApplicationListener<TransactionEvent> {
  private static final long CLIENT_TRANSACTION_LIMIT = 1000L;
  private static final int AMOUNT_IN_WALLET = 0;

  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;

  @Autowired
  public TransactionEventListener(TransactionRepository transactionRepository,
                                  WalletRepository walletRepository) {
    this.transactionRepository = transactionRepository;
    this.walletRepository = walletRepository;
  }

  @Override
  public void onApplicationEvent(TransactionEvent event) {
    final Transaction transaction = event.getTransaction();
    final BigDecimal amount = transaction.getAmount();

    if (amount.compareTo(BigDecimal.valueOf(CLIENT_TRANSACTION_LIMIT)) >= AMOUNT_IN_WALLET) {
      defineTransaction(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
      throw new TransactionDomainException(MessageErrors.CLIENT_EXCEED_LIMIT_PER_TRANSACTION);
    }

    switch (event.getType()) {
      case SEND:
        final Wallet wallet = getWallet(transaction, transaction.getTargetClientId(),
            transaction.getTargetWalletId());

        if (wallet.getBalance().compareTo(amount) <= AMOUNT_IN_WALLET) {
          defineTransaction(transaction, TransactionStatus.FAIL, MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
          throw new TransactionDomainException(MessageErrors.CLIENT_HAS_NO_SUFFICIENT_BALANCE);
        }

        updateWallet(wallet, TransactionType.SEND, amount);
        break;
      case RECEIVE:
        final Wallet targetWallet = getWallet(transaction, transaction.getClientId(),
            transaction.getWalletId());

        updateWallet(targetWallet, TransactionType.RECEIVE, amount);
        break;
      default:
        throw new TransactionDomainException(MessageErrors.UNKNOWN_TRANSACTION_TYPE);
    }
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

  private void updateWallet(Wallet targetWallet, TransactionType type, BigDecimal transactionAmount) {
    targetWallet.setBalance(calculateBalance(type, targetWallet.getBalance(), transactionAmount));
    log.debug("Transaction type: {} \t amount: {} \t oldbalance: {}\tbalance: {}",
        type.name(), transactionAmount, targetWallet.getBalance(), targetWallet.getBalance());
    walletRepository.save(targetWallet);
  }

  private Wallet getWallet(final Transaction transaction, UUID clientId, UUID walletId) {
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
}
