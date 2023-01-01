package br.com.itau.transferapi.domain;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class ClientProvider {
    public static Client getCreatedClient() {
        final UUID clientID = UUID.randomUUID();
        return Client.builder()
                .id(clientID)
                .name("ClientName")
                .build();
    }

    public static Wallet getCreatedWallet(final UUID clientID) {
        return Wallet.builder(clientID, UUID.randomUUID())
                .status(WalletStatus.CREATED)
                .balance(BigDecimal.ZERO)
                .build();
    }
}
