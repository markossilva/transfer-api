package br.com.itau.transferapi.application.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClientParams {
  private final String id;
  private final String name;
  private final double initialBalance;

  public CreateClientParams(String id, String name, double initialBalance) {
    this.id = id;
    this.name = name;
    this.initialBalance = initialBalance;
  }
}
