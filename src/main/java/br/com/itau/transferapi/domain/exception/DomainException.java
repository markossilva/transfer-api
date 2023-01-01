package br.com.itau.transferapi.domain.exception;

public class DomainException extends RuntimeException {
    public DomainException(final MessageErrors exceptionMessage) {
        super(exceptionMessage.getMessage());
    }
}
