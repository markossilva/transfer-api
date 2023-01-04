package br.com.itau.transferapi.domain.exception;

public final class TransactionDomainException extends DomainException {
  public TransactionDomainException(MessageErrors exceptionMessage) {
    super(exceptionMessage);
  }
}
