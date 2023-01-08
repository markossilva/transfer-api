package br.com.itau.transferapi.domain;

import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.TransactionStatus;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TransactionProvider {

  public static List<Transaction> getCreatedTransactions(
      final UUID originClientId,
      final UUID originWalletId,
      final UUID targetClientId,
      final UUID targetWalletId) {
    return Arrays.asList(
        Transaction.builder()
            .id(BigInteger.valueOf(1L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(2L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(3L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(4L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(5L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(6L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(7L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(8L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(9L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.TEN)
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(11L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build(),
        Transaction.builder()
            .id(BigInteger.valueOf(12L))
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .status(TransactionStatus.PROCESSING)
            .amount(BigDecimal.ONE)
            .date(LocalDateTime.now())
            .build()
    );
  }

  public static Wallet getCreatedWallet(final UUID clientId, final UUID walletId) {
    return Wallet.builder()
        .id(walletId)
        .clientId(clientId)
        .balance(BigDecimal.valueOf(20L))
        .status(WalletStatus.ACTIVE)
        .build();
  }
}
