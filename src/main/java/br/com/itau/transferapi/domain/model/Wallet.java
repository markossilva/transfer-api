package br.com.itau.transferapi.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(builderMethodName = "internalBuilder")
@Getter
public class Wallet {
    private UUID id;
    private BigDecimal balance;
    private WalletStatus status;
    private UUID clientId;

    public static WalletBuilder builder(final UUID clientId, final UUID walletId) {
        return internalBuilder()
                .id(walletId)
                .clientId(clientId);
    }
}
