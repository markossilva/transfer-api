package br.com.itau.transferapi.domain.exception;

/**
 * Class represents the client exception layer, extends od {@link DomainException}
 *
 * @author markos
 */
public final class ClientDomainException extends DomainException {

  /**
   * This Exception handle a client layer and make a custom message on flow of exceptions.
   *
   * @param exceptionMessage is a custom and centralized hub of message errors of {@link MessageErrors}
   */
  public ClientDomainException(MessageErrors exceptionMessage) {
    super(exceptionMessage);
  }
}
