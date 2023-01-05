package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.infrastracture.repository.relational.model.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, UUID> {
  @Modifying
  @Query("update WalletEntity w set w.status = :status where w.id = :walletId and w.client.id = :clientId")
  int updateWalletSetStatusForClientIdAndWalletId(
      @Param("status") WalletStatus status,
      @Param("walletId") UUID walletId,
      @Param("clientId") UUID clientId);

  @Query("select w from WalletEntity w where w.client.id = ?1 and w.id = ?2")
  WalletEntity findByClientAndWallet(UUID clientId, UUID walletId);

  @Query("select w from WalletEntity w where w.client.id = ?1")
  List<WalletEntity> findByClientId(UUID clientId);
}
