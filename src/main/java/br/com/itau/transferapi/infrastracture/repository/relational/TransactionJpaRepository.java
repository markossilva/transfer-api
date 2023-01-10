package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.WalletTransactions;
import br.com.itau.transferapi.infrastracture.repository.relational.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, BigInteger> {
  @Query("SELECT u FROM TransactionEntity u WHERE u.client.id = ?1")
  List<TransactionEntity> findAllByClientId(UUID clientID);

  @Query("SELECT u FROM TransactionEntity u WHERE u.client.id = ?1 and u.wallet.id = ?2")
  List<TransactionEntity> findAllByClientIdAndWallet(UUID clientID, UUID walletID);

  @Query("select " +
      "new br.com.itau.transferapi.domain.model.WalletTransactions(t.amount, ts.name as status, t.type, t.cause, t.date) " +
      "from WalletEntity w " +
      "left join TransactionEntity t on w.id = t.walletId " +
      "left join TransactionStatusEntity ts on t.status = ts.id " +
      "where w.id = ?1 order by t.date desc ")
  List<WalletTransactions> findAllByWalletId(UUID walletID);
}
