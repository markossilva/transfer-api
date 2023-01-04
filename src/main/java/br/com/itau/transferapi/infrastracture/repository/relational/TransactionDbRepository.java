package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
class TransactionDbRepository implements TransactionRepository {

  private final TransactionJpaRepository transactionJpaRepository;
  private final ObjectMapper mapper;

  @Autowired
  public TransactionDbRepository(TransactionJpaRepository transactionJpaRepository, ObjectMapper mapper) {
    this.transactionJpaRepository = transactionJpaRepository;
    this.mapper = mapper;
  }

  @Override
  public Optional<List<Transaction>> findAllByClientId(UUID clientID) {
    return Optional.of(transactionJpaRepository.findAllByClientId(clientID)
        .stream()
        .map(transactionEntity -> mapper.convertValue(transactionEntity, Transaction.class))
        .collect(Collectors.toList()));
  }

  @Override
  public Optional<List<Transaction>> findAllByClientIdAndWallet(UUID clientID, UUID walletID) {
    return Optional.of(transactionJpaRepository.findAllByClientIdAndWallet(clientID, walletID)
        .stream()
        .map(transactionEntity -> mapper.convertValue(transactionEntity, Transaction.class))
        .collect(Collectors.toList()));
  }

  @Override
  public BigInteger save(Transaction transaction) {
    return transactionJpaRepository
        .save(mapper.convertValue(transaction, TransactionEntity.class))
        .getId();
  }

  @Override
  public Transaction update(Transaction transaction) {
    transactionJpaRepository
        .save(mapper.convertValue(transaction, TransactionEntity.class));
    return transaction;
  }
}
