package br.com.itau.transferapi.infrastracture.repository.relational.model;

import br.com.itau.transferapi.domain.model.TransactionStatus;
import br.com.itau.transferapi.domain.model.TransactionType;
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
        TARGET_WALLET_ID = "origin_wallet_id",
  TARGET_CLIENT_ID = "origin_client_id",
      JOIN_CLIENT_ID = "client_id",
      JOIN_WALLET_ID = "wallet_id";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @Column(name = JOIN_CLIENT_ID, columnDefinition = "uuid")
  private UUID clientId;

  @Column(name = JOIN_WALLET_ID, columnDefinition = "uuid")
  private UUID walletId;
  @ManyToOne(targetEntity = ClientEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(name = JOIN_CLIENT_ID, nullable = false, insertable = false, updatable = false)
  private ClientEntity client;

  @ManyToOne(targetEntity = WalletEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(name = JOIN_WALLET_ID, nullable = false, insertable = false, updatable = false)
  private WalletEntity wallet;

  @Column(name = TARGET_CLIENT_ID, nullable = false, columnDefinition = "uuid")
  private UUID originClientId;

  @Column(name = TARGET_WALLET_ID, nullable = false, columnDefinition = "uuid")
  private UUID originWalletId;

  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private TransactionStatus status;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private TransactionType type;

  private String cause;

  private LocalDateTime date;

  public TransactionEntity() {
  }

  public TransactionEntity(BigInteger id, UUID clientId,
                           UUID walletId, ClientEntity client,
                           WalletEntity wallet, UUID originClientId,
                           UUID originWalletId, BigDecimal amount,
                           TransactionStatus status, TransactionType type,
                           String cause, LocalDateTime date) {
    this.id = id;
    this.clientId = clientId;
    this.walletId = walletId;
    this.client = client;
    this.wallet = wallet;
    this.originClientId = originClientId;
    this.originWalletId = originWalletId;
    this.amount = amount;
    this.status = status;
    this.type = type;
    this.cause = cause;
    this.date = date;
  }
}
