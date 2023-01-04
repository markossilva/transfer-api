package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
class ClientDbRepository implements ClientRepository {

  private final ClientJpaRepository clientJpaRepository;
  private final ObjectMapper mapper;

  @Autowired
  public ClientDbRepository(ClientJpaRepository clientJpaRepository, ObjectMapper mapper) {
    this.clientJpaRepository = clientJpaRepository;
    this.mapper = mapper;
  }

  @Override
  public Optional<Client> findById(UUID id) {
    return clientJpaRepository.findById(id)
        .map(clientEntity -> mapper.convertValue(clientEntity, Client.class));
  }

  @Override
  public void save(Client client) {
    clientJpaRepository.save(mapper.convertValue(client, ClientEntity.class));
  }

  @Override
  public List<Client> findAll() {
    return clientJpaRepository.findAll()
        .stream()
        .map(clientEntity -> mapper.convertValue(clientEntity, Client.class))
        .collect(Collectors.toList());
  }
}
