package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Getter
@Setter
public class Transaction {
  BigInteger id;
  UUID targetClientId;
  UUID targetWalletId;
  UUID clientId;
  UUID walletId;
  BigDecimal amount;
  TransactionStatus status;
  TransactionType type;
  String cause;
  LocalDateTime date;

  public Transaction() {
  }

  public Transaction(BigInteger id, UUID targetClientId, UUID targetWalletId,
                     UUID clientId, UUID walletId, BigDecimal amount,
                     TransactionStatus status, TransactionType type,
                     String cause, LocalDateTime date) {
    this.id = id;
    this.targetClientId = targetClientId;
    this.targetWalletId = targetWalletId;
    this.clientId = clientId;
    this.walletId = walletId;
    this.amount = amount;
    this.status = status;
    this.type = type;
    this.cause = cause;
    this.date = date;
  }
}
