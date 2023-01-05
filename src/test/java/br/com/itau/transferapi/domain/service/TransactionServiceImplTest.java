package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.TransactionProvider;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.TransactionStatus;
import br.com.itau.transferapi.domain.model.TransactionType;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.domain.service.impl.ClientServiceImpl;
import br.com.itau.transferapi.domain.service.impl.TransactionServiceImpl;
import br.com.itau.transferapi.infrastracture.repository.memory.MemoryDbWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  private ClientRepository clientRepository;
  private TransactionService transactionService;
  private ClientService clientService;

  @BeforeEach
  void setUp() {
    walletRepository = new MemoryDbWalletRepository();
    transactionRepository = mock(TransactionRepository.class);
    clientRepository = mock(ClientRepository.class);

    transactionService = new TransactionServiceImpl(transactionRepository, walletRepository);
    clientService = new ClientServiceImpl(clientRepository, walletRepository);

    walletRepository.save(originWallet);
    walletRepository.save(targetWallet);
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

    final BigDecimal expectedValue = BigDecimal.valueOf(32L);
    assertThat(clientService.findAWallet(targetClientId, targetWalletId).getBalance())
        .describedAs("Verify target wallet [%s] balance should be [%d]", targetWalletId, expectedValue)
        .isEqualTo(expectedValue);

    final BigDecimal expectedOriginValue = BigDecimal.valueOf(8L);
    assertThat(clientService.findAWallet(originClientId, originWalletId).getBalance())
        .describedAs("Verify origin wallet [%s] balance should be [%d]", originWallet, expectedOriginValue)
        .isEqualTo(expectedOriginValue);
  }

  private void doTransaction(List<Transaction> partition) {
    partition.forEach(transaction -> {
      transactionService.doTransaction(transaction);
    });
  }

  @Test
  void shouldDoTransaction_moreThanLimitOfTransaction_thenThrowException() {
    final Executable executable = () -> transactionService.doTransaction(
        Transaction.builder()
            .id(BigInteger.ONE)
            .originClientId(originClientId)
            .originWalletId(originWalletId)
            .targetClientId(targetClientId)
            .targetWalletId(targetWalletId)
            .amount(BigDecimal.valueOf(1001L))
            .status(TransactionStatus.PROCESSING)
            .date(LocalDateTime.now())
            .build());

    verify(transactionRepository, times(0))
        .save(any(Transaction.class));
    assertThrows(RuntimeException.class, executable);
  }

  @Test
  void shouldDoTransaction_moreThanWalletAmount_thenThrowException() {
    final Executable executable = () -> transactionService.doTransaction(
        Transaction.builder()
            .id(BigInteger.ONE)
            .originClientId(originClientId)
            .originWalletId(originWalletId)
            .targetClientId(targetClientId)
            .targetWalletId(targetWalletId)
            .amount(BigDecimal.valueOf(100L))
            .status(TransactionStatus.PROCESSING)
            .date(LocalDateTime.now())
            .build());

    verify(transactionRepository, times(0))
        .save(any(Transaction.class));
    assertThrows(RuntimeException.class, executable);
  }

  @Test
  void shouldDoTransaction_whenWalletNotExists_thenThrowException() {
    walletRepository.delete(targetClientId, targetWalletId);

    final Executable executable = () -> transactionService.doTransaction(
        Transaction.builder()
            .id(BigInteger.ONE)
            .originClientId(originClientId)
            .originWalletId(originWalletId)
            .targetClientId(targetClientId)
            .targetWalletId(targetWalletId)
            .amount(BigDecimal.ONE)
            .status(TransactionStatus.PROCESSING)
            .date(LocalDateTime.now())
            .build());

    verify(transactionRepository, times(0))
        .save(any(Transaction.class));
    assertThrows(RuntimeException.class, executable);
  }

  @Test
  void shouldCalculateBalance_thenThrowException() {
    final Executable executable = () -> getCalculateBalanceMethod()
        .invoke(transactionService, TransactionType.UNKNOWN, BigDecimal.ZERO, BigDecimal.ZERO);

    assertThrows(InvocationTargetException.class, executable);
  }

  private Method getCalculateBalanceMethod() throws NoSuchMethodException {
    Method method = transactionService.getClass().getDeclaredMethod("calculateBalance", TransactionType.class, BigDecimal.class, BigDecimal.class);
    method.setAccessible(true);
    return method;
  }
}
