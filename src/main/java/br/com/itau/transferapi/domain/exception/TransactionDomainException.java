package br.com.itau.transferapi.domain.exception;

/**
 * Class represents the transaction exception layer, extends of {@link DomainException}
 *
 * @author markos
 */
public final class TransactionDomainException extends DomainException {
  /**
   * This Exception handle a transaction layer and make a custom message on flow of exceptions.
   *
   * @param messageErrors is a custom and centralized hub of message errors of {@link MessageErrors}
   */
  public TransactionDomainException(MessageErrors messageErrors) {
    super(messageErrors);
  }
}
