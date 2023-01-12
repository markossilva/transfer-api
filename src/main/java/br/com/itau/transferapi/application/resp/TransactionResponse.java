package br.com.itau.transferapi.application.resp;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class TransactionResponse {
  private BigInteger transactionId;
  private BigDecimal amount;

  public TransactionResponse(BigInteger transactionId, BigDecimal amount) {
    this.transactionId = transactionId;
    this.amount = amount;
  }
}
