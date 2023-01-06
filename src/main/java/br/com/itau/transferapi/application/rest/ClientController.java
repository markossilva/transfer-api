package br.com.itau.transferapi.application.rest;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.RegisteredClient;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {
  private final ClientService service;

  @Autowired
  public ClientController(ClientService service) {
    this.service = service;
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

  @GetMapping("/user/wallets")
  ResponseEntity<List<Wallet>> findAllWalletsByClient(@RequestParam final String clientId) {
    return ResponseEntity.ok(service.findAllWallets(UUID.fromString(clientId)));
  }

  @GetMapping("/wallet")
  ResponseEntity<Wallet> findWalletsById(@RequestParam final String clientId, @RequestParam final String walletId) {
    return ResponseEntity.ok(service.findAWallet(UUID.fromString(clientId), UUID.fromString(walletId)));
  }
}
