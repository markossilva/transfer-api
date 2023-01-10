package br.com.itau.transferapi.application.req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class TransactionParams {
  private final String originClientId;
  private final String originWalletId;
  private final String targetClientId;
  private final String targetWalletId;
  private final BigDecimal amount;
}
