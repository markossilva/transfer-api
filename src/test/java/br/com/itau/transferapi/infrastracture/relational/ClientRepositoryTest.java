package br.com.itau.transferapi.infrastracture.relational;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.ClientDbRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.model.ClientEntity;
import br.com.itau.transferapi.infrastracture.repository.relational.ClientJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ClientRepositoryTest {
  private ModelMapper mapper;
  private ClientJpaRepository clientJpaRepository;
  private ClientRepository clientDbRepository;

  @BeforeEach
  void setUp() {
    clientJpaRepository = mock(ClientJpaRepository.class);
    mapper = new ModelMapper();
    clientDbRepository = new ClientDbRepository(clientJpaRepository, mapper);
  }

  @Test
  void whenFindById_thenReturn_findClient() {
    final Client client = Client.builder()
        .id(UUID.randomUUID())
        .name("test")
        .wallets(new ArrayList<>())
        .transactions(new ArrayList<>())
        .build();
    when(clientJpaRepository.findById(client.getId()))
        .thenReturn(Optional.of(mapper.map(client, ClientEntity.class)));
    final Optional<Client> byId = clientDbRepository.findById(client.getId());

    verify(clientJpaRepository).findById(any(UUID.class));
    assertNotNull(byId.get());
  }

  @Test
  void shouldCreateClient_thenSaveEntity() {
    final Client client = Client.builder()
        .id(UUID.randomUUID())
        .name("test")
        .wallets(new ArrayList<>())
        .transactions(new ArrayList<>())
        .build();
    clientDbRepository.save(client);

    verify(clientJpaRepository).save(any(ClientEntity.class));
  }

  @Test
  void whenFindAll_thenReturn_allClients() {
    final ClientEntity clientEntity = ClientEntity.builder()
        .id(UUID.randomUUID())
        .name("test")
        .wallets(new ArrayList<>())
        .transactions(new ArrayList<>())
        .build();

    when(clientJpaRepository.findAll())
        .thenReturn(Collections.singletonList(clientEntity));

    final List<Client> all = clientDbRepository.findAll();
    assertTrue(all.stream().findFirst().get()
        .getId().equals(clientEntity
            .getId()));
  }
}
