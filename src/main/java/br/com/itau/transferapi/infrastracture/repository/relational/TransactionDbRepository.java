package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.infrastracture.repository.relational.model.TransactionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionDbRepository implements TransactionRepository {

  private final TransactionJpaRepository transactionJpaRepository;
  private final ModelMapper mapper;

  @Autowired
  public TransactionDbRepository(TransactionJpaRepository transactionJpaRepository, ModelMapper mapper) {
    this.transactionJpaRepository = transactionJpaRepository;
    this.mapper = mapper;
    mapper.createTypeMap(TransactionEntity.class, Transaction.class)
        .addMappings(to -> {
          to.map(TransactionEntity::getId, Transaction::setId);
          to.map(TransactionEntity::getTargetClientId, Transaction::setTargetClientId);
          to.map(TransactionEntity::getTargetWalletId, Transaction::setTargetWalletId);
          to.map(TransactionEntity::getClientId, Transaction::setClientId);
          to.map(TransactionEntity::getWalletId, Transaction::setWalletId);
          to.map(TransactionEntity::getAmount, Transaction::setAmount);
          to.map(TransactionEntity::getStatus, Transaction::setStatus);
          to.map(TransactionEntity::getCause, Transaction::setCause);
          to.map(TransactionEntity::getDate, Transaction::setDate);
        });
  }

  @Override
  public Optional<List<Transaction>> findAllByClientId(UUID clientID) {
    return Optional.of(transactionJpaRepository.findAllByClientId(clientID)
        .stream()
        .map(transactionEntity -> mapper.map(transactionEntity, Transaction.class))
        .collect(Collectors.toList()));
  }

  @Override
  public Optional<Transaction> findAllByClientIdAndWallet(UUID clientID, UUID walletID) {
    return Optional.of(mapper.map(transactionJpaRepository
        .findAllByClientIdAndWallet(clientID, walletID), Transaction.class));
  }

  @Override
  public Transaction save(Transaction transaction) {
    final TransactionEntity savedTransaction = transactionJpaRepository
        .save(mapper.map(transaction, TransactionEntity.class));
    return mapper.map(savedTransaction, Transaction.class);
  }
}
