package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.infrastracture.repository.relational.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {
  @Query("select c from ClientEntity c left join c.wallets w where c.id = w.walletClientId and w.id = ?1")
  ClientEntity findClientByWalletId(UUID walletId);
}
