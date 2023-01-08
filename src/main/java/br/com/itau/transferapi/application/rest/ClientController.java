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
  ResponseEntity<Boolean> doTransaction(@RequestParam final String originClientId,
                                        @RequestParam final String originWalletId,
                                        @RequestParam final String targetClientId,
                                        @RequestParam final String targetWalletId,
                                        @RequestParam final BigDecimal amount) {
    transactionService.doTransaction(Transaction.builder()
        .clientId(UUID.fromString(targetClientId))
        .walletId(UUID.fromString(targetWalletId))
        .targetClientId(UUID.fromString(originClientId))
        .targetWalletId(UUID.fromString(originWalletId))
        .status(TransactionStatus.PROCESSING)
        .amount(amount)
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
