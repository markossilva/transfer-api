package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Client {
  private UUID id;
  private String name;
  private List<Wallet> wallet;
  private List<Transaction> transactions;
}
