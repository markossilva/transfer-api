package br.com.itau.transferapi.application.rest;

import br.com.itau.transferapi.domain.exception.ApplicationException;
import br.com.itau.transferapi.domain.model.*;
import br.com.itau.transferapi.domain.service.ClientService;
import br.com.itau.transferapi.domain.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {
  private static final Logger logger = LogManager.getLogger(ClientController.class);

  private static final String CREATE = "/create";
  private static final String ALL_CLIENTS = "/all";
  private static final String TRANSFER_VALUE = "/transfer/amount";
  private static final String WALLETS_BY_CLIENT = "/{clientId}/wallets";
  private static final String BY_WALLET_AND_CLIENT_ID = "/{clientId}/wallet/{walletId}";
  private static final String CLIENT_BY_WALLET_ID = "/wallet/{walletId}";
  private static final String CLIENT_WALLET_TRANSACTIONS = "/wallet/transactions/{walletId}";
  private final ClientService service;
  private final TransactionService transactionService;

  @Autowired
  public ClientController(ClientService service, TransactionService transactionService) {
    this.service = service;
    this.transactionService = transactionService;
  }

  @PostMapping(
      path = CREATE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<RegisteredClient> createClient(@RequestBody final Object createOrderRequest) {
    final RegisteredClient clientTest = service
        .createNewClient(Client.builder()
            .id(UUID.randomUUID())
            .name("Markos Test")
            .build());

    return ResponseEntity.ok(clientTest);
  }

  @GetMapping(path = ALL_CLIENTS)
  ResponseEntity<List<Client>> findAllClients() {
    return ResponseEntity.ok(service.findAllClients());
  }

  @PutMapping(path = TRANSFER_VALUE)
  ResponseEntity<BigInteger> doTransaction(@RequestParam final String originClientId,
                                           @RequestParam final String originWalletId,
                                           @RequestParam final String targetClientId,
                                           @RequestParam final String targetWalletId,
                                           @RequestParam final BigDecimal amount) {
    final Transaction buildTransaction = Transaction.builder()
        .clientId(UUID.fromString(targetClientId))
        .walletId(UUID.fromString(targetWalletId))
        .targetClientId(UUID.fromString(originClientId))
        .targetWalletId(UUID.fromString(originWalletId))
        .status(TransactionStatus.PROCESSING)
        .type(TransactionType.SEND)
        .amount(amount)
        .build();
    transactionService.doTransaction(buildTransaction);
    return ResponseEntity.ok(buildTransaction.getId());
  }

  @GetMapping(path = WALLETS_BY_CLIENT)
  ResponseEntity<List<Wallet>> findAllWalletsByClient(@RequestParam final String clientId) {
    return ResponseEntity.ok(service.findAllWallets(UUID.fromString(clientId)));
  }

  @GetMapping(path = BY_WALLET_AND_CLIENT_ID)
  ResponseEntity<Wallet> findWalletsById(@RequestParam final String clientId, @RequestParam final String walletId) {
    try {
      return ResponseEntity.ok(service
          .findAWallet(UUID.fromString(clientId), UUID.fromString(walletId)));
    } catch (Exception e) {
      logger.error("Error to findWalletsById: wallet#{} - {}. Message: {}", clientId, walletId, e.getMessage());
      throw new ApplicationException(e);
    }
  }

  @GetMapping(path = CLIENT_BY_WALLET_ID)
  ResponseEntity<Client> findClientByWalletId(@RequestParam final String walletId) {
    try {
      return ResponseEntity.ok(service.findClientByWalletId(UUID.fromString(walletId)));
    } catch (Exception e) {
      logger.error("Error to findClientByWalletId: wallet#{} - {}", walletId, e.getMessage());
      throw new ApplicationException(e);
    }
  }

  @GetMapping(path = CLIENT_WALLET_TRANSACTIONS)
  ResponseEntity<List<WalletTransactions>> findAllByWalletId(@PathVariable final String walletId) {
    try {
      return ResponseEntity.ok(transactionService
          .findAllByWalletId(UUID.fromString(walletId)));
    } catch (Exception e) {
      logger.error("Error to findAllByWalletId: wallet#{} - {}", walletId, e.getMessage());
      throw new ApplicationException(e);
    }
  }
}
