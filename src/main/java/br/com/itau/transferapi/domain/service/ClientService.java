package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.RegisteredClient;
import br.com.itau.transferapi.domain.model.Wallet;

import java.util.List;
import java.util.UUID;

/**
 * Client service: provides all information about client and your wallets
 *
 * @author markos
 */
public interface ClientService {
  /**
   * Create a new client and wallet
   *
   * @param client an object representation to create a new client
   * @return a client identifier and wallet identifier of the created client. {@link RegisteredClient}
   */
  RegisteredClient createNewClient(final Client client);

  /**
   * Find all clients, showing all wallets and transactions
   *
   * @return list of registered clients. {@link Client}
   */
  List<Client> findAllClients();

  /**
   * Create a new wallet from informed client identifier
   *
   * @param clientId client identifier
   * @param wallet   object to create for the client specified. {@link Wallet}
   * @return a new create {@link Wallet}
   * @throws br.com.itau.transferapi.domain.exception.ClientDomainException
   */
  Wallet createNewWallet(final UUID clientId, final Wallet wallet);

  /**
   * Find all client wallets based on informed client identifier
   *
   * @param clientID client identifier
   * @return a list of client {@link Wallet}
   * @throws br.com.itau.transferapi.domain.exception.ClientDomainException
   */
  List<Wallet> findAllWallets(final UUID clientID);

  /**
   * Find a specific wallet based on client and wallet identifier.
   *
   * @param clientId client identifier
   * @param walletId wallet identifier
   * @return a found client {@link Wallet}
   * @throws br.com.itau.transferapi.domain.exception.ClientDomainException
   */
  Wallet findAWallet(final UUID clientId, final UUID walletId);

  /**
   * Find a client base in informed wallet identifier
   *
   * @param walletId wallet identifier
   * @return {@link Client} found
   * @throws br.com.itau.transferapi.domain.exception.ClientDomainException
   */
  Client findClientByWalletId(final UUID walletId);
}
