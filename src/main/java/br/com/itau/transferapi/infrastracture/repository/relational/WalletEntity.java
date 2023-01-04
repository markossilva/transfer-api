package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.WalletStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = WalletEntity.TABLE)
class WalletEntity {
  static final String TABLE = "tbl_wallet",
      CLIENT_JOIN = "client_id";

  @Id
  private UUID id;

  private BigDecimal balance;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private WalletStatus status;

  @ManyToOne
  @JoinColumn(name = CLIENT_JOIN, nullable = false)
  private ClientEntity client;
}
