package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Transaction {
    private UUID id;
    private UUID clientId;
    private UUID walletId;
    private BigDecimal value;
    private TransactionStatus status;
    private TransactionType type;
    private LocalDateTime createdAt;
}
