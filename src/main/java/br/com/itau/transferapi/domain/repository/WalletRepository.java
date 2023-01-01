package br.com.itau.transferapi.domain.repository;

import br.com.itau.transferapi.domain.model.Wallet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
  Optional<Wallet> findById(final UUID clientId, final UUID walletId);

  Optional<List<Wallet>> findByClientId(final UUID clientId);

  Wallet save(final Wallet wallet);
}
