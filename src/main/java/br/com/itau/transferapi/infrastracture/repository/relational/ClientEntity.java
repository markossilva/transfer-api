package br.com.itau.transferapi.infrastracture.repository.relational;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = ClientEntity.TABLE)
class ClientEntity {
  static final String TABLE = "tbl_client",
  CLIENT_JOIN = "client";

  @Id
  private UUID id;

  private String name;

  @OneToMany(mappedBy = CLIENT_JOIN)
  private List<WalletEntity> wallets;

  @OneToMany(mappedBy = CLIENT_JOIN)
  private List<TransactionEntity> transactions;
}
