package br.com.itau.transferapi.domain.exception;

/**
 * Class represents the exception domain lauer
 *
 * @author markos
 */
public abstract class DomainException extends RuntimeException {

  /**
   * Domain exception handle all domain layer exceptions and make a custom message on flow of exceptions.
   *
   * @param exceptionMessage is a custom and centralized hub of message errors of {@link MessageErrors}
   */
  public DomainException(final MessageErrors exceptionMessage) {
    super(exceptionMessage.getMessage());
  }
}
