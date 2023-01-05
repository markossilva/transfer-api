package br.com.itau.transferapi.infrastracture.repository.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, BigInteger> {
  @Query("SELECT u FROM TransactionEntity u WHERE u.client.id = ?1")
  List<TransactionEntity> findAllByClientId(UUID clientID);

  @Query("SELECT u FROM TransactionEntity u WHERE u.client.id = ?1 and u.wallet.id = ?2")
  TransactionEntity findAllByClientIdAndWallet(UUID clientID, UUID walletID);
}
