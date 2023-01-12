package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletTransactions {
  private final BigDecimal amount;
  private final String status;
  private final TransactionType type;
  private final String cause;
  private final LocalDateTime date;
}
