package br.com.itau.transferapi.domain.exception;

public final class ClientDomainException extends DomainException {
  public ClientDomainException(MessageErrors exceptionMessage) {
    super(exceptionMessage);
  }
}
