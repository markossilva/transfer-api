package br.com.itau.transferapi.application.rest;

import br.com.itau.transferapi.domain.model.*;
import br.com.itau.transferapi.domain.service.ClientService;
import br.com.itau.transferapi.domain.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {
  private final ClientService service;
  private final TransactionService transactionService;

  @Autowired
  public ClientController(ClientService service, TransactionService transactionService) {
    this.service = service;
    this.transactionService = transactionService;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<RegisteredClient> createClient(@RequestBody final Object createOrderRequest) {
    final RegisteredClient clientTest = service
        .createNewClient(Client.builder()
            .id(UUID.randomUUID())
            .name("Markos Test")
            .build());

    return ResponseEntity.ok(clientTest);
  }

  @GetMapping
  ResponseEntity<List<Client>> findAllClients() {
    return ResponseEntity.ok(service.findAllClients());
  }

  @PutMapping
  ResponseEntity<Boolean> doTransaction() {
    transactionService.doTransaction(Transaction.builder()
        .targetClientId(UUID.fromString("09966449-2460-487a-800a-19cc3a2b88a9"))
        .targetWalletId(UUID.fromString("137c24cb-827f-44c2-8410-0cd474da78b3"))
        .originClientId(UUID.fromString("80383279-68c2-402c-a9fa-1caaf843a536"))
        .originWalletId(UUID.fromString("edb563af-024a-45b8-bfa9-8d7ca46d8c4d"))
        .status(TransactionStatus.PROCESSING)
        .amount(BigDecimal.ONE)
        .build());
    return ResponseEntity.ok(Boolean.TRUE);
  }

  @GetMapping("/wallets")
  ResponseEntity<List<Wallet>> findAllWalletsByClient(@RequestParam final String clientId) {
    return ResponseEntity.ok(service.findAllWallets(UUID.fromString(clientId)));
  }

  @GetMapping("/wallet")
  ResponseEntity<Wallet> findWalletsById(@RequestParam final String clientId, @RequestParam final String walletId) {
    return ResponseEntity.ok(service.findAWallet(UUID.fromString(clientId), UUID.fromString(walletId)));
  }

  @GetMapping("/wallet/id")
  ResponseEntity<Client> findClientByWalletId(@RequestParam final String walletId) {
    return ResponseEntity.ok(service.findClientByWalletId(UUID.fromString(walletId)));
  }
}
