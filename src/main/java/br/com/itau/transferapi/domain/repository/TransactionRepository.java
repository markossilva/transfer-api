package br.com.itau.transferapi.domain.repository;

import br.com.itau.transferapi.domain.model.Transaction;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
  Optional<List<Transaction>> findAllByClientId(UUID clientID);

  Optional<List<Transaction>> findAllByClientIdAndWallet(UUID clientID, UUID walletID);

  BigInteger save(Transaction transaction);
  Transaction update(Transaction transaction);
}
