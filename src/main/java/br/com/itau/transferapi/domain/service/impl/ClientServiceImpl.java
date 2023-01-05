package br.com.itau.transferapi.domain.service.impl;

import br.com.itau.transferapi.domain.exception.ClientDomainException;
import br.com.itau.transferapi.domain.exception.MessageErrors;
import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.RegisteredClient;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
  private static final BigDecimal INITIAL_WALLET_BALANCE = BigDecimal.ZERO;

  private final ClientRepository clientRepository;
  private final WalletRepository walletRepository;

  @Override
  public RegisteredClient createNewClient(final Client client) {
    final UUID clientId = client.getId();

    clientRepository.findById(clientId).ifPresent(s -> {
      throw new ClientDomainException(MessageErrors.CLIENT_ALREADY_REGISTERED);
    });

    final Wallet clientWallet = Wallet.builder()
        .id(UUID.randomUUID())
        .clientId(clientId)
        .balance(INITIAL_WALLET_BALANCE)
        .status(WalletStatus.ACTIVE)
        .build();

    client.setWallets(Collections.singletonList(clientWallet));

    clientRepository.save(client);

    return RegisteredClient.builder()
        .clientId(clientWallet.getClientId())
        .walletId(clientWallet.getId())
        .build();
  }

  @Override
  public List<Client> findAllClients() {
    return clientRepository.findAll();
  }

  @Override
  public Wallet createNewWallet(final UUID clientId, final Wallet wallet) {
    final Client client = clientRepository.findById(clientId)
        .orElseThrow(() -> new ClientDomainException(MessageErrors.CLIENT_HAS_NO_EXISTS));

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
}
