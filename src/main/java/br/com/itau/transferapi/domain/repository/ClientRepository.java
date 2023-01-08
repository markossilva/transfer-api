package br.com.itau.transferapi.domain.repository;

import br.com.itau.transferapi.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

  Optional<Client> findById(UUID id);

  void save(Client client);

  List<Client> findAll();

  Optional<Client> findClientByWalletId(UUID walletId);
}
