package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.TransactionProvider;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.infrastracture.repository.memory.MemoryDbWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
public class TransactionServiceImplTest {

  private static final int THREAD_COUNT = 3;
  private final UUID originClientId = UUID.randomUUID();
  private final UUID originWalletId = UUID.randomUUID();
  private final UUID targetClientId = UUID.randomUUID();
  private final UUID targetWalletId = UUID.randomUUID();
  private final List<Transaction> transactionList = TransactionProvider
      .getCreatedTransactions(originClientId, originWalletId, targetClientId, targetWalletId);
  private final Wallet originWallet = TransactionProvider.getCreatedWallet(originClientId, originWalletId);
  private final Wallet targetWallet = TransactionProvider.getCreatedWallet(targetClientId, targetWalletId);
  private WalletRepository walletRepository;
  private TransactionRepository transactionRepository;
  private TransactionService service;

  @BeforeEach
  void setUp() {
    walletRepository = new MemoryDbWalletRepository();
    transactionRepository = mock(TransactionRepository.class);

    service = new TransactionServiceImpl(walletRepository, transactionRepository);

    walletRepository.save(originWallet);
    walletRepository.save(targetWallet);
  }

  @Test
  void shouldCreateTransaction_thenDoTransaction() {
    final UUID originClientId = UUID.randomUUID();
    final UUID originWalletId = UUID.randomUUID();
    final Wallet originWallet = TransactionProvider
        .getCreatedWallet(originClientId, originWalletId);

    final UUID targetClientId = UUID.randomUUID();
    final UUID targetWalletId = UUID.randomUUID();
    final Wallet targetWallet = TransactionProvider
        .getCreatedWallet(targetClientId, targetWalletId);

    final Transaction transaction = TransactionProvider
        .getCreatedTransaction(originClientId, originWalletId, targetClientId, targetWalletId);

    when(walletRepository.findById(originClientId, originWalletId))
        .thenReturn(Optional.of(originWallet));
    when(walletRepository.findById(targetClientId, targetWalletId))
        .thenReturn(Optional.of(targetWallet));
    when(transactionRepository.save(transaction))
        .thenReturn(transaction.getId());

    final BigInteger transactionId = service.doTransaction(transaction);

    verify(walletRepository, times(2)).save(any(Wallet.class));
    assertEquals(transactionId, transaction.getId());
  }

  @RepeatedTest(10)
  void shouldCreateConcurrent_thenDoTransaction() throws InterruptedException {

    final List<List<Transaction>> partitions = ListUtils.partition(transactionList, transactionList.size() / THREAD_COUNT);

    final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    long startTime = System.nanoTime();
    partitions.forEach(partition ->
        executorService.execute(() ->
            doTransaction(partition)

        )
    );

    executorService.shutdown();

    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
      log.warn("Thread execution timeout while await for termination");
    }

    log.info("execution time: {} ms", (System.nanoTime() - startTime) / 1_000_000);
  }

  private void doTransaction(List<Transaction> partition) {
    partition.forEach(transaction -> {
      service.doTransaction(transaction);
    });
  }
}
