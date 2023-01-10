package br.com.itau.transferapi.domain.exception;

/**
 * Class represents the not found exception, extends of {@link DomainException}
 */
public final class NotFoundDomainException extends DomainException {
  /**
   * Domain exception handle all domain layer exceptions when not found throws
   * and make a custom message on flow of exceptions.
   *
   * @param messageErrors is a custom and centralized hub of message errors of {@link MessageErrors}
   */
  public NotFoundDomainException(MessageErrors messageErrors) {
    super(messageErrors);
  }
}
