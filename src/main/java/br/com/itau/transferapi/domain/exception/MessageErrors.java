package br.com.itau.transferapi.domain.exception;

import lombok.Getter;

@Getter
public enum MessageErrors {
  EMPTY(""),
  CLIENT_HAS_EXISTS("Client with given id doesn't exist."),
  CLIENT_HAS_NO_WALLETS("Client with given id don't have any wallet."),
  CLIENT_HAS_NO_TRANSACTIONS("Client with given id don't have any transaction."),
  DIFFERENT_WALLET_CLIENT_ID("Client ID to be have same in wallet."),
  CLIENT_OR_WALLET_NOT_EXISTS("Client or wallet not exists."),
  CLIENT_HAS_NO_SUFFICIENT_BALANCE("Insufficient account balance."),
  UNKNOWN_TRANSACTION_TYPE("Unknown type of transaction, please retry again."),
  WALLET_UPDATE_CANT_BE_NULL("Wallet to update can't be null."),
  TRANSACTION_SUCCESS("Transaction effectived with success."),
  CLIENT_EXCEED_LIMIT_PER_TRANSACTION("Client exceed limit per transaction");
  private final String message;

  MessageErrors(final String message) {
    this.message = message;
  }
}
