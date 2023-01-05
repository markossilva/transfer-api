package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Getter
@Setter
public class Transaction {
  BigInteger id;
  UUID originClientId;
  UUID originWalletId;
  UUID targetClientId;
  UUID targetWalletId;
  BigDecimal amount;
  TransactionStatus status;
  String cause;
  LocalDateTime date;

  public Transaction() {}
  public Transaction(BigInteger id, UUID originClientId,
                     UUID originWalletId, UUID targetClientId,
                     UUID targetWalletId, BigDecimal amount,
                     TransactionStatus status, String cause,
                     LocalDateTime date) {
    this.id = id;
    this.originClientId = originClientId;
    this.originWalletId = originWalletId;
    this.targetClientId = targetClientId;
    this.targetWalletId = targetWalletId;
    this.amount = amount;
    this.status = status;
    this.cause = cause;
    this.date = date;
  }
}
