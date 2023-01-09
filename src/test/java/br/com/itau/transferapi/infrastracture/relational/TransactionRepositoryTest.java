package br.com.itau.transferapi.infrastracture.relational;

import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.TransactionStatus;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.TransactionDbRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.TransactionJpaRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.model.ClientEntity;
import br.com.itau.transferapi.infrastracture.repository.relational.model.TransactionEntity;
import br.com.itau.transferapi.infrastracture.repository.relational.model.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionRepositoryTest {
  private TransactionJpaRepository transactionJpaRepository;
  private TransactionRepository transactionRepository;
  private UUID clientId;
  private UUID walletId;
  private TransactionEntity transactionEntity;
  private Transaction transaction;
  private ModelMapper mapper;

  @BeforeEach
  void setUp() {
    transactionJpaRepository = mock(TransactionJpaRepository.class);
    mapper = spy(new ModelMapper());
    mapper.getConfiguration().setAmbiguityIgnored(Boolean.TRUE);
    transactionRepository = new TransactionDbRepository(transactionJpaRepository, mapper);

    clientId = UUID.randomUUID();
    walletId = UUID.randomUUID();
    final ClientEntity user = ClientEntity.builder()
        .id(clientId)
        .name("nameUser")
        .build();
    final UUID targetClientId = UUID.randomUUID();
    final UUID targetWalletId = UUID.randomUUID();

    transactionEntity = TransactionEntity.builder()
        .id(BigInteger.ONE)
        .client(user)
        .wallet(WalletEntity.builder()
            .id(walletId)
            .client(user)
            .balance(BigDecimal.TEN)
            .status(WalletStatus.ACTIVE)
            .build())
        .targetClientId(clientId)
        .targetWalletId(walletId)
        .clientId(targetClientId)
        .walletId(targetWalletId)
        .amount(BigDecimal.TEN)
        .status(TransactionStatus.SUCCESS)
        .date(LocalDateTime.now())
        .build();

    transaction = Transaction.builder()
        .id(BigInteger.ONE)
        .targetClientId(clientId)
        .targetWalletId(walletId)
        .clientId(targetClientId)
        .walletId(targetWalletId)
        .amount(BigDecimal.TEN)
        .status(TransactionStatus.SUCCESS)
        .date(LocalDateTime.now())
        .build();
  }

  @Test
  void whenFindAllByClientId_thenReturnAllTransactions() {
    when(transactionJpaRepository.findAllByClientId(clientId))
        .thenReturn(Collections.singletonList(transactionEntity));

    final Optional<List<Transaction>> allByClientId = transactionRepository
        .findAllByClientId(clientId);

    assertTrue(allByClientId.isPresent());
    assertSame(allByClientId.get().stream()
        .findFirst().get().getStatus(), TransactionStatus.SUCCESS);
  }

  @Test
  void whenFindByClientIdAndWallet_thenReturnATransactions() {
    when(transactionJpaRepository.findAllByClientIdAndWallet(clientId, walletId))
        .thenReturn(transactionEntity);

    final Optional<Transaction> allByClientIdAndWallet = transactionRepository
        .findAllByClientIdAndWallet(clientId, walletId);

    assertTrue(allByClientIdAndWallet.isPresent());
    assertEquals(allByClientIdAndWallet.get().getTargetWalletId(), transactionEntity.getWallet().getId());
  }

  @Test
  void whenSave_thenReturnId() {
    when(transactionJpaRepository.save(transactionEntity))
        .thenReturn(transactionEntity);
    when(mapper.map(transaction, TransactionEntity.class))
        .thenReturn(transactionEntity);

    final Transaction save = transactionRepository.save(transaction);

    verify(transactionJpaRepository).save(any(TransactionEntity.class));
    assertEquals(save.getId(), transactionEntity.getId());
  }
}
