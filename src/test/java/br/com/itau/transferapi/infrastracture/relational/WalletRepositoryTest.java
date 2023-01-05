package br.com.itau.transferapi.infrastracture.relational;

import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.WalletDbRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.model.WalletEntity;
import br.com.itau.transferapi.infrastracture.repository.relational.WalletJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WalletRepositoryTest {
  private WalletJpaRepository walletJpaRepository;
  private WalletRepository walletRepository;

  private UUID clientId;
  private UUID walletId;
  private WalletEntity walletEntity;
  private Wallet wallet;
  private static final int WHEN_DELETE_SUCCESS = 0;
  private static final int WHEN_DELETE_FAIL = 1;

  @BeforeEach
  void setUp() {
    walletJpaRepository = mock(WalletJpaRepository.class);
    walletRepository = new WalletDbRepository(walletJpaRepository, new ModelMapper());

    clientId = UUID.randomUUID();
    walletId = UUID.randomUUID();
    walletEntity = WalletEntity.builder()
        .id(walletId)
        .walletClientId(clientId)
        .balance(BigDecimal.TEN)
        .status(WalletStatus.ACTIVE)
        .build();
    wallet = Wallet.builder()
        .id(walletId)
        .balance(BigDecimal.TEN)
        .status(WalletStatus.ACTIVE)
        .clientId(clientId)
        .build();
  }

  @Test
  void whenFindAllByClientId_thenReturnAllWallets() {
    when(walletJpaRepository.findByClientId(clientId))
        .thenReturn(Collections.singletonList(walletEntity));

    final Optional<List<Wallet>> byClientId = walletRepository.findByClientId(clientId);
    assertTrue(byClientId.isPresent());
    assertSame(byClientId.get().stream().findFirst().get().getStatus(), WalletStatus.ACTIVE);
  }

  @Test
  void whenFindByClientIdAndWallet_thenReturnAWallet() {
    when(walletJpaRepository.findByClientAndWallet(clientId, walletId))
        .thenReturn(walletEntity);

    final Optional<Wallet> allByClientIdAndWallet = walletRepository
        .findById(clientId, walletId);

    assertTrue(allByClientIdAndWallet.isPresent());
    assertEquals(allByClientIdAndWallet.get().getId(), walletEntity.getId());
    assertEquals(allByClientIdAndWallet.get().getClientId(), walletEntity.getWalletClientId());
  }

  @Test
  void whenSave_thenReturnId() {
    when(walletJpaRepository.save(walletEntity))
        .thenReturn(walletEntity);

    final Wallet save = walletRepository.save(wallet);

    verify(walletJpaRepository).save(any(WalletEntity.class));
    assertEquals(save.getId(), walletEntity.getId());
  }

  @Test
  void whenDelete_thenReturnSuccess() {
    when(walletJpaRepository
        .updateWalletSetStatusForClientIdAndWalletId(WalletStatus.INACTIVE, walletId, clientId))
        .thenReturn(WHEN_DELETE_SUCCESS);

    final boolean delete = walletRepository.delete(clientId, walletId);

    verify(walletJpaRepository)
        .updateWalletSetStatusForClientIdAndWalletId(
            any(WalletStatus.class), any(UUID.class), any(UUID.class));
    assertTrue(delete);
  }

  @Test
  void whenDelete_thenReturnFail() {
    when(walletJpaRepository
        .updateWalletSetStatusForClientIdAndWalletId(WalletStatus.INACTIVE, walletId, clientId))
        .thenReturn(WHEN_DELETE_FAIL);

    final boolean delete = walletRepository.delete(clientId, walletId);

    verify(walletJpaRepository)
        .updateWalletSetStatusForClientIdAndWalletId(
            any(WalletStatus.class), any(UUID.class), any(UUID.class));
    assertTrue(!delete);
  }
}
