package br.com.itau.transferapi.domain;

import br.com.itau.transferapi.domain.model.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    public static List<Transaction> getCreatedTransactions(final UUID originClientId, final UUID targetClientId) {
        return Arrays.asList(
                Transaction.builder()
                        .id(BigInteger.ONE)
                        .originClientId(originClientId)
                        .type(TransactionType.SEND)
                        .targetClientId(targetClientId)
                        .targetWalletId(UUID.randomUUID())
                        .value(BigDecimal.TEN)
                        .status(TransactionStatus.SUCCESS)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Transaction.builder()
                        .id(BigInteger.TWO)
                        .originClientId(targetClientId)
                        .type(TransactionType.RECEIVE)
                        .targetClientId(originClientId)
                        .targetWalletId(UUID.randomUUID())
                        .value(BigDecimal.TEN)
                        .status(TransactionStatus.SUCCESS)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
