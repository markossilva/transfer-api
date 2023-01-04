package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
class WalletDbRepository implements WalletRepository {

  private static final int NUMBER_OF_ROWS = 0;
  private final WalletJpaRepository walletJpaRepository;
  private final ObjectMapper mapper;

  @Autowired
  public WalletDbRepository(WalletJpaRepository walletJpaRepository, ObjectMapper mapper) {
    this.walletJpaRepository = walletJpaRepository;
    this.mapper = mapper;
  }

  @Override
  public Optional<Wallet> findById(UUID clientId, UUID walletId) {
    return Optional.of(mapper.convertValue(walletJpaRepository
        .findByClientAndWallet(clientId, walletId), Wallet.class));
  }

  @Override
  public Optional<List<Wallet>> findByClientId(UUID clientId) {
    return Optional.of(walletJpaRepository.findByClientId(clientId)
        .stream()
        .map(walletEntity -> mapper.convertValue(walletEntity, Wallet.class))
        .collect(Collectors.toList()));
  }

  @Override
  public Wallet save(Wallet wallet) {
    walletJpaRepository
        .save(mapper.convertValue(wallet, WalletEntity.class));
    return wallet;
  }

  @Override
  public boolean delete(UUID clientId, UUID walletId) {
    return walletJpaRepository
        .updateWalletSetStatusForClientIdAndWalletId(
            WalletStatus.INACTIVE, walletId, clientId) > NUMBER_OF_ROWS;
  }
}
