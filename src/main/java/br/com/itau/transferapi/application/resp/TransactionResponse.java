package br.com.itau.transferapi.application.resp;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Getter
public class TransactionResponse {
  private BigInteger transactionId;
  private BigDecimal amount;
}
