package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.RegisteredClient;
import br.com.itau.transferapi.domain.model.Wallet;

import java.util.List;
import java.util.UUID;

public interface ClientService {
  RegisteredClient createNewClient(final Client client);

  List<Client> findAllClients();

  Wallet createNewWallet(final UUID customerID, final Wallet wallet);

  List<Wallet> findAllWallets(final UUID clientID);

  Wallet findAWallet(final UUID clientId, final UUID walletId);

  Client findClientByWalletId(final UUID walletId);
}
