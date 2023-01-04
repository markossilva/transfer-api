package br.com.itau.transferapi.infrastracture.repository.memory;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MemoryDBClientRepository implements ClientRepository {
  @Override
  public Optional<Client> findById(UUID id) {
    return Optional.of(MemoryDBProvider.memoryClient.get(id));
  }

  @Override
  public void save(Client client) {
    MemoryDBProvider.memoryClient.put(client.getId(), client);
  }

  @Override
  public List<Client> findAll() {
    return new ArrayList<>(MemoryDBProvider.memoryClient.values());
  }
}
