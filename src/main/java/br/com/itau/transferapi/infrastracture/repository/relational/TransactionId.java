package br.com.itau.transferapi.infrastracture.repository.relational;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@RequiredArgsConstructor
class TransactionId implements Serializable {
  private final UUID clientId;
  private final UUID walletId;
}
