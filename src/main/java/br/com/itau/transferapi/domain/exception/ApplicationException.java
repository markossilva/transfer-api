package br.com.itau.transferapi.domain.exception;

/**
 * Class represents the application exception layer, extends of {@link RuntimeException}
 *
 * @author markos
 */
public final class ApplicationException extends RuntimeException {
  /**
   * This Exception handle a application layer and make a custom message on flow of exceptions.
   *
   * @param exception caught exception
   */
  public ApplicationException(Exception exception) {
    super(exception.getMessage(), exception);
  }
}
