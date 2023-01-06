package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@Builder
public class TransactionEvent extends ApplicationEvent {
  private Transaction transaction;
  private TransactionType type;

  public TransactionEvent(Transaction transaction, TransactionType type) {
    super(type);
    this.transaction = transaction;
    this.type = type;
  }
}
