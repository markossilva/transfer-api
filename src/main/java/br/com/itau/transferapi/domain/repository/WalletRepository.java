package br.com.itau.transferapi.domain.repository;

import br.com.itau.transferapi.domain.model.Wallet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Optional<Wallet> findById(final UUID clientID, final UUID walletID);

    Optional<List<Wallet>> findByClientId(final UUID clientID);

    Wallet save(final Wallet wallet);
}
