package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.model.ClientEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClientDbRepository implements ClientRepository {

  private final ClientJpaRepository clientJpaRepository;
  private final ModelMapper mapper;

  @Autowired
  public ClientDbRepository(ClientJpaRepository clientJpaRepository, ModelMapper mapper) {
    this.clientJpaRepository = clientJpaRepository;
    this.mapper = mapper;
  }

  @Override
  public Optional<Client> findById(UUID id) {
    return clientJpaRepository.findById(id)
        .map(clientEntity -> mapper.map(clientEntity, Client.class));
  }

  @Override
  public void save(Client client) {
    clientJpaRepository.save(mapper.map(client, ClientEntity.class));
  }

  @Override
  public List<Client> findAll() {
    return clientJpaRepository.findAll()
        .stream()
        .map(clientEntity -> mapper.map(clientEntity, Client.class))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Client> findClientByWalletId(UUID walletId) {
    return Optional.of(mapper.map(clientJpaRepository
        .findClientByWalletId(walletId), Client.class));
  }
}
