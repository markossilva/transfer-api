package br.com.itau.transferapi.infrastracture.repository.relational.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = WalletStatusEntity.TABLE)
public class WalletStatusEntity {
  static final String TABLE = "tbl_wallet_status";
  @Id
  private Integer id;
  private String name;
}
