package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.model.WalletEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WalletDbRepository implements WalletRepository {

  private static final int NUMBER_OF_ROWS = 0;
  private final WalletJpaRepository walletJpaRepository;
  private final ModelMapper mapper;

  @Autowired
  public WalletDbRepository(WalletJpaRepository walletJpaRepository, ModelMapper mapper) {
    this.walletJpaRepository = walletJpaRepository;
    this.mapper = mapper;
    mapper.createTypeMap(WalletEntity.class, Wallet.class)
        .addMappings(to -> {
          to.map(WalletEntity::getId, Wallet::setId);
          to.map(WalletEntity::getBalance, Wallet::setBalance);
          to.map(WalletEntity::getStatus, Wallet::setStatus);
          to.map(WalletEntity::getWalletClientId, Wallet::setClientId);
        });
  }

  @Override
  public Optional<Wallet> findById(UUID clientId, UUID walletId) {
    return Optional.of(mapper.map(walletJpaRepository
        .findByClientAndWallet(clientId, walletId), Wallet.class));
  }

  @Override
  public Optional<List<Wallet>> findByClientId(UUID clientId) {
    return Optional.of(walletJpaRepository.findByClientId(clientId)
        .stream()
        .map(walletEntity -> mapper.map(walletEntity, Wallet.class))
        .collect(Collectors.toList()));
  }

  @Override
  public Wallet save(Wallet wallet) {
    walletJpaRepository
        .save(mapper.map(wallet, WalletEntity.class));
    return wallet;
  }

  @Override
  public boolean delete(UUID clientId, UUID walletId) {
    return walletJpaRepository
        .updateWalletSetStatusForClientIdAndWalletId(
            WalletStatus.INACTIVE, walletId, clientId) == NUMBER_OF_ROWS;
  }
}
