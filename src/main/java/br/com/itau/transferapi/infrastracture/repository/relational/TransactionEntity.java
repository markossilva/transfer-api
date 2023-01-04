package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = TransactionEntity.TABLE)
class TransactionEntity {
  static final String TABLE = "tbl_transaction",
      TARGET_WALLET_ID = "target_wallet_id",
      TARGET_CLIENT_ID = "target_client_id",
      JOIN_CLIENT_ID = "client_id",
      JOIN_WALLET_ID = "wallet_id";

  @Id
  private BigInteger id;

  @ManyToOne
  @JoinColumn(name = JOIN_CLIENT_ID, nullable = false)
  private ClientEntity client;

  @ManyToOne
  @JoinColumn(name = JOIN_WALLET_ID, nullable = false)
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
}
