package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Transaction {
  private BigInteger id;
  private UUID originClientId;
  private UUID targetClientId;
  private UUID targetWalletId;
  private BigDecimal value;
  private TransactionStatus status;
  private TransactionType type;
  private LocalDateTime createdAt;
}
