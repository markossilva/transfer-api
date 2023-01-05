package br.com.itau.transferapi.infrastracture.repository.relational.model;

import br.com.itau.transferapi.domain.model.WalletStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@Entity
@Table(name = WalletEntity.TABLE)
public
class WalletEntity {
  static final String TABLE = "tbl_wallet",
      CLIENT_JOIN = "client_id";

  @Id
  private UUID id;

  private BigDecimal balance;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private WalletStatus status;

  @Column(name = CLIENT_JOIN)
  private UUID walletClientId;

  @ManyToOne(targetEntity = ClientEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(name = CLIENT_JOIN, nullable = false, insertable = false, updatable = false)
  private ClientEntity client;

  public WalletEntity() {
  }

  public WalletEntity(UUID id, BigDecimal balance, WalletStatus status, UUID walletClientId, ClientEntity client) {
    this.id = id;
    this.balance = balance;
    this.status = status;
    this.walletClientId = walletClientId;
    this.client = client;
  }

  public WalletEntity(UUID id, BigDecimal balance, WalletStatus status, ClientEntity client) {
    this.id = id;
    this.balance = balance;
    this.status = status;
    this.client = client;
  }
}
