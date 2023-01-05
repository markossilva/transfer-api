package br.com.itau.transferapi.infrastracture.repository.relational;

import br.com.itau.transferapi.infrastracture.repository.relational.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {
}
