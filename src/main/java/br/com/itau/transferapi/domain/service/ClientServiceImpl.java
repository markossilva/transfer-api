package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.exception.ClientDomainException;
import br.com.itau.transferapi.domain.exception.MessageErrors;
import br.com.itau.transferapi.domain.exception.TransactionDomainException;
import br.com.itau.transferapi.domain.model.*;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ClientServiceImpl implements ClientService, TransactionService {
  private static final BigDecimal INITIAL_WALLET_BALANCE = BigDecimal.ZERO;
  private static final long CLIENT_TRANSACTION_LIMIT = 1000L;
  private static final int ZERO_VALUE_COMPARATOR = 0;

  private final ClientRepository clientRepository;
  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;

  public ClientServiceImpl(final ClientRepository clientRepository,
                           final TransactionRepository transactionRepository,
                           final WalletRepository walletRepository) {
    this.clientRepository = clientRepository;
    this.transactionRepository = transactionRepository;
    this.walletRepository = walletRepository;
  }

  public ClientServiceImpl(final TransactionRepository transactionRepository,
                           final WalletRepository walletRepository) {
    this.transactionRepository = transactionRepository;
    this.walletRepository = walletRepository;
    this.clientRepository = null;
  }

  @Override
  public UUID createNewClient(final Client client) {
    final Wallet clientWallet = Wallet.builder(client.getId(), UUID.randomUUID())
        .balance(INITIAL_WALLET_BALANCE)
        .status(WalletStatus.ACTIVE)
        .build();

    client.setWallet(Collections.singletonList(clientWallet));

    clientRepository.save(client);

    return client.getId();
  }

  @Override
  public Wallet createNewWallet(final UUID clientId, final Wallet wallet) {
    final Client client = clientRepository.findById(clientId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_HAS_EXISTS));

    if (!client.getId().equals(wallet.getClientId())) {
      throw new ClientDomainException(MessageErrors.DIFFERENT_WALLET_CLIENT_ID);
    }

    return walletRepository.save(wallet);
  }

  @Override
  public List<Wallet> findAllWallets(UUID clientId) {
    return walletRepository.findByClientId(clientId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_HAS_NO_WALLETS));
  }

  @Override
  public Wallet findAWallet(UUID clientId, UUID walletId) {
    return walletRepository.findById(clientId, walletId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_OR_WALLET_NOT_EXISTS));
  }

  @Override
  public List<Transaction> findAllTransactions(UUID clientId) {
    return transactionRepository.findAllByClientId(clientId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_HAS_NO_TRANSACTIONS));
  }

  @Override
  public List<Transaction> findAllTransactionsByWallet(UUID clientId, UUID walletId) {
    return transactionRepository.findAllByClientIdAndWallet(clientId, walletId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_HAS_NO_TRANSACTIONS));
  }

  @Override
  public BigInteger doTransaction(Transaction transaction) {
    final BigDecimal transactionAmount = transaction.getAmount();
    final BigInteger transactionId = transactionRepository.save(transaction);

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
    return transactionId;
  }

  private void updateWallet(Wallet targetWallet, TransactionType type, BigDecimal transactionAmount) {
    final Wallet wallet = targetWallet.toBuilder()
        .balance(calculateBalance(type, targetWallet.getBalance(), transactionAmount))
        .build();
    log.debug("Transaction type: {} \t amount: {} \t oldbalance: {}\tbalance: {}",
        type.name(), transactionAmount, targetWallet.getBalance(), wallet.getBalance());
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
