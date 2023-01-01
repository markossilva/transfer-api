package br.com.itau.transferapi.domain.exception;

import lombok.Getter;

@Getter
public enum MessageErrors {
    CLIENT_HAS_EXISTS("Client with given id doesn't exist"),
    CLIENT_HAS_NO_WALLETS("Client with given id don't have any wallet"),
    CLIENT_HAS_NO_TRANSACTIONS("Client with given id don't have any transaction"),
    DIFFERENT_WALLET_CLIENT_ID("Client ID to be have same in wallet");
    private final String message;

    MessageErrors(final String message) {
        this.message = message;
    }
}
