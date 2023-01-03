package br.com.itau.transferapi.infrastracture.repository.memory;

import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MemoryDBTransactionRepository implements TransactionRepository {
  @Override
  public Optional<List<Transaction>> findAllByClientId(UUID clientID) {
    return Optional.empty();
  }

  @Override
  public Optional<List<Transaction>> findAllByClientIdAndWallet(UUID clientID, UUID walletID) {
    return Optional.empty();
  }

  @Override
  public BigInteger save(Transaction transaction) {
    return null;
  }

  @Override
  public Transaction update(Transaction transaction) {
    return null;
  }
}
