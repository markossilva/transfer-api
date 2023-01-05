package br.com.itau.transferapi.domain.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Client {
  private UUID id;
  private String name;
  private List<Wallet> wallets;
  private List<Transaction> transactions;

  public Client() {}
  public Client(UUID id, String name, List<Wallet> wallets, List<Transaction> transactions) {
    this.id = id;
    this.name = name;
    this.wallets = wallets;
    this.transactions = transactions;
  }
}
