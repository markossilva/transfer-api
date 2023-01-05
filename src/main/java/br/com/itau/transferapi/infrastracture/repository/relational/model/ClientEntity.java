package br.com.itau.transferapi.infrastracture.repository.relational.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@Table(name = ClientEntity.TABLE)
public class ClientEntity {
  static final String TABLE = "tbl_client",
  CLIENT_JOIN = "client";

  @Id
  private UUID id;

  private String name;

  @OneToMany(mappedBy = CLIENT_JOIN)
  private List<WalletEntity> wallets;

  @OneToMany(mappedBy = CLIENT_JOIN)
  private List<TransactionEntity> transactions;

  public ClientEntity(){}
  public ClientEntity(UUID id, String name, List<WalletEntity> wallets, List<TransactionEntity> transactions) {
    this.id = id;
    this.name = name;
    this.wallets = wallets;
    this.transactions = transactions;
  }
}
