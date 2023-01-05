package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Wallet {
  private UUID id;
  private BigDecimal balance;
  private WalletStatus status;
  private UUID clientId;

  public Wallet() {}
  public Wallet(UUID id, BigDecimal balance, WalletStatus status, UUID clientId) {
    this.id = id;
    this.balance = balance;
    this.status = status;
    this.clientId = clientId;
  }
}
