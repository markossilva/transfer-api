package br.com.itau.transferapi.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private UUID clientId;
    private UUID walletId;
    private BigDecimal value;
    private TransactionStatus status;
    private TransactionType type;
    private LocalDateTime createdAt;
}
