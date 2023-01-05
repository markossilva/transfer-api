package br.com.itau.transferapi.infrastracture.repository.relational.model;

import br.com.itau.transferapi.domain.model.TransactionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = TransactionEntity.TABLE)
public
class TransactionEntity {
  static final String TABLE = "tbl_transaction",
      TARGET_WALLET_ID = "target_wallet_id",
      TARGET_CLIENT_ID = "target_client_id",
      JOIN_CLIENT_ID = "client_id",
      JOIN_WALLET_ID = "wallet_id";

  @Id
  private BigInteger id;

  @Column(name = JOIN_CLIENT_ID)
  private UUID originClientId;

  @Column(name = JOIN_WALLET_ID)
  private UUID originWalletId;
  @ManyToOne(targetEntity = ClientEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(name = JOIN_CLIENT_ID, nullable = false, insertable = false, updatable = false)
  private ClientEntity client;

  @ManyToOne(targetEntity = WalletEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(name = JOIN_WALLET_ID, nullable = false, insertable = false, updatable = false)
  private WalletEntity wallet;

  @Column(name = TARGET_CLIENT_ID, nullable = false)
  private UUID targetClientId;

  @Column(name = TARGET_WALLET_ID, nullable = false)
  private UUID targetWalletId;

  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private TransactionStatus status;

  private String cause;

  private LocalDateTime date;

  public TransactionEntity() {
  }

  public TransactionEntity(BigInteger id, UUID originClientId, UUID originWalletId,
                           ClientEntity client, WalletEntity wallet, UUID targetClientId,
                           UUID targetWalletId, BigDecimal amount, TransactionStatus status,
                           String cause, LocalDateTime date) {
    this.id = id;
    this.originClientId = originClientId;
    this.originWalletId = originWalletId;
    this.client = client;
    this.wallet = wallet;
    this.targetClientId = targetClientId;
    this.targetWalletId = targetWalletId;
    this.amount = amount;
    this.status = status;
    this.cause = cause;
    this.date = date;
  }

  public TransactionEntity(BigInteger id, ClientEntity client,
                           WalletEntity wallet, UUID targetClientId,
                           UUID targetWalletId, BigDecimal amount,
                           TransactionStatus status, String cause,
                           LocalDateTime date) {
    this.id = id;
    this.client = client;
    this.wallet = wallet;
    this.targetClientId = targetClientId;
    this.targetWalletId = targetWalletId;
    this.amount = amount;
    this.status = status;
    this.cause = cause;
    this.date = date;
  }
}
