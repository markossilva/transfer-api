package br.com.itau.transferapi.infrastracture.repository.relational.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TransactionStatusEntity.TABLE)
public class TransactionStatusEntity {
  static final String TABLE = "tbl_transaction_status";

  @Id
  private Integer id;
  private String name;
}
