package br.com.itau.transferapi.infrastracture.relational;

import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.WalletDbRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.WalletEntity;
import br.com.itau.transferapi.infrastracture.repository.relational.WalletJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalletRepositoryTest {
  private WalletJpaRepository walletJpaRepository;
  private WalletRepository walletRepository;

  private UUID clientId;
  private UUID walletId;
  private WalletEntity walletEntity;

//  @BeforeEach
  void setUp() {
    walletJpaRepository = mock(WalletJpaRepository.class);
    walletRepository = new WalletDbRepository(walletJpaRepository, new ModelMapper());

    clientId = UUID.randomUUID();
    walletId = UUID.randomUUID();
    walletEntity = WalletEntity.builder()
        .id(walletId)
        .clientId(clientId)
        .balance(BigDecimal.TEN)
        .status(WalletStatus.ACTIVE)
        .build();
  }

//  @Test
  void whenFindAllByClientId_thenReturnAllWallets() {
    when(walletJpaRepository.findByClientId(clientId))
        .thenReturn(Collections.singletonList(walletEntity));

    final Optional<List<Wallet>> byClientId = walletRepository.findByClientId(clientId);
    assertTrue(byClientId.isPresent());
    assertSame(byClientId.get().stream().findFirst().get().getStatus(), WalletStatus.INACTIVE);
  }
}
