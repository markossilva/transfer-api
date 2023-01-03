package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Value
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
}
