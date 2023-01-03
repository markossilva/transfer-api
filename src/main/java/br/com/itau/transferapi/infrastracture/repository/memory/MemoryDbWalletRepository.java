package br.com.itau.transferapi.infrastracture.repository.memory;

import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemoryDbWalletRepository implements WalletRepository {
  private static final Map<UUID, List<Wallet>> memoryWallet = new HashMap<>();

  @Override
  public Optional<Wallet> findById(UUID clientId, UUID walletId) {
    final List<Wallet> wallets = memoryWallet.get(clientId);

    for (Wallet wallet : wallets) {
      if (wallet.getId() == walletId && wallet.getClientId() == clientId) {
        return Optional.of(wallet);
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<List<Wallet>> findByClientId(UUID clientId) {
    return Optional.of(memoryWallet.get(clientId));
  }

  @Override
  public Wallet save(Wallet wallet) {
    List<Wallet> wallets = memoryWallet.get(wallet.getClientId());
    if (wallets == null) {
      wallets = new ArrayList<>();
    }

    wallets.removeIf(next -> next.getId() == next.getId() && next.getClientId() == wallet.getClientId());

    wallets.add(wallet);

    memoryWallet.put(wallet.getClientId(), wallets);
    return wallet;
  }
}
