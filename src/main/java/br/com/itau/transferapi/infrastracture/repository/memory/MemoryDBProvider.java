package br.com.itau.transferapi.infrastracture.repository.memory;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.Wallet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

final class MemoryDBProvider {
  public static final Map<UUID, List<Wallet>> memoryWallet = new HashMap<>();
  public static final Map<UUID, Client> memoryClient = new HashMap<>();
  public static final Map<UUID, List<Transaction>> memoryTransaction = new HashMap<>();
}
