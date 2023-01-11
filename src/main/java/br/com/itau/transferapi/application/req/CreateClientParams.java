package br.com.itau.transferapi.application.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClientParams {
  private final String name;
  private final double initialBalance;

  public CreateClientParams(String name, double initialBalance) {
    this.name = name;
    this.initialBalance = initialBalance;
  }
}
