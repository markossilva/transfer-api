package br.com.itau.transferapi.infrastracture.repository.relational;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {
}
