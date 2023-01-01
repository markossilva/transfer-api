package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.exception.DomainException;
import br.com.itau.transferapi.domain.exception.MessageErrors;
import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
  private static final BigDecimal INITIAL_WALLET_BALANCE = BigDecimal.ZERO;

  private final ClientRepository clientRepository;
  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;

  @Override
  public UUID createNewClient(final Client client) {
    final Wallet clientWallet = Wallet.builder(client.getId(), UUID.randomUUID())
        .balance(INITIAL_WALLET_BALANCE)
        .status(WalletStatus.CREATED)
        .build();

    client.setWallet(Collections.singletonList(clientWallet));

    clientRepository.save(client);

    return client.getId();
  }

  @Override
  public Wallet createNewWallet(final UUID clientId, final Wallet wallet) {
    final Client client = clientRepository.findById(clientId)
        .orElseThrow(() -> new DomainException(MessageErrors.CLIENT_HAS_EXISTS));

    if (!client.getId().equals(wallet.getClientId())) {
      throw new DomainException(MessageErrors.DIFFERENT_WALLET_CLIENT_ID);
    }

    return walletRepository.save(wallet);
  }

  @Override
  public List<Wallet> findAllWallets(UUID clientId) {
    return walletRepository.findByClientId(clientId)
        .orElseThrow(() -> new DomainException(MessageErrors.CLIENT_HAS_NO_WALLETS));
  }

  @Override
  public List<Transaction> findAllTransactions(UUID clientId) {
    return transactionRepository.findAllByClientId(clientId)
        .orElseThrow(() -> new DomainException(MessageErrors.CLIENT_HAS_NO_TRANSACTIONS));
  }

  @Override
  public List<Transaction> findAllTransactionsByWallet(UUID clientId, UUID walletId) {
    return transactionRepository.findAllByClientIdAndWallet(clientId, walletId)
        .orElseThrow(() -> new DomainException(MessageErrors.CLIENT_HAS_NO_TRANSACTIONS));
  }
}
