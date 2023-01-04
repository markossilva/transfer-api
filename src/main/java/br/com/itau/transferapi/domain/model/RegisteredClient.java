package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public final class RegisteredClient {
  private UUID clientId;
  private UUID walletId;
}
