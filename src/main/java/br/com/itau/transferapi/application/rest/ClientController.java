package br.com.itau.transferapi.application.rest;

import br.com.itau.transferapi.application.req.CreateClientParams;
import br.com.itau.transferapi.application.req.TransactionParams;
import br.com.itau.transferapi.application.resp.TransactionResponse;
import br.com.itau.transferapi.domain.exception.ApplicationException;
import br.com.itau.transferapi.domain.model.*;
import br.com.itau.transferapi.domain.service.ClientService;
import br.com.itau.transferapi.domain.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/client")
public class ClientController {
  private static final Logger logger = LogManager.getLogger(ClientController.class);

  private static final String CREATE = "/create",
      ALL_CLIENTS = "/all",
      TRANSFER_VALUE = "/transfer/amount",
      WALLETS_BY_CLIENT = "/{clientId}/wallets",
      BY_WALLET_AND_CLIENT_ID = "/{clientId}/wallet/{walletId}",
      CLIENT_BY_WALLET_ID = "/wallet/{walletId}",
      CLIENT_WALLET_TRANSACTIONS = "/wallet/transactions/{walletId}";

  private final ClientService service;
  private final TransactionService transactionService;

  @Autowired
  public ClientController(ClientService service, TransactionService transactionService) {
    this.service = service;
    this.transactionService = transactionService;
  }

  @Operation(summary = "Create a new client with a wallet")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Return a registered client and wallet identifier",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = RegisteredClient.class))
          }
      )
  })
  @PostMapping(
      path = CREATE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<RegisteredClient> createClient(@RequestBody CreateClientParams createClientParams) {
    try {

      final Client client = Client.builder()
          .id(UUID.randomUUID())
          .name(createClientParams.getName())
          .build();

      final Wallet clientWallet = Wallet.builder()
          .id(UUID.randomUUID())
          .clientId(client.getId())
          .balance(BigDecimal.valueOf(createClientParams.getInitialBalance()))
          .status(WalletStatus.ACTIVE)
          .build();

      client.setWallets(Collections.singletonList(clientWallet));

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(service.createNewClient(client));
    } catch (IllegalArgumentException e) {
      logger.error("Error to createClient: {}", e.getMessage());
      throw new ApplicationException(e);
    }
  }

  @Operation(summary = "Returns all clients with your respective wallets and transactions")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Return a list of registered clients",
          content = {
              @Content(mediaType = "application/json")
          }
      ),
      @ApiResponse(responseCode = "400", description = "business layer error",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "list of clients not found",
          content = @Content(mediaType = "application/json"))
  })
  @GetMapping(path = ALL_CLIENTS)
  ResponseEntity<List<Client>> findAllClients() {
    return ResponseEntity.ok(service.findAllClients());
  }

  @Operation(summary = "Performs a amount transfer between accounts and return the transaction identifier")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Return the transaction identifier",
          content = {
              @Content(mediaType = "application/json")
          }
      ),
      @ApiResponse(responseCode = "400", description = "business layer error",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "error to create transaction",
          content = @Content(mediaType = "application/json"))
  })
  @PutMapping(path = TRANSFER_VALUE)
  ResponseEntity<TransactionResponse> doTransaction(@RequestBody TransactionParams params) {
    try {
      final Transaction buildTransaction = Transaction.builder()
          .clientId(UUID.fromString(params.getTargetClientId()))
          .walletId(UUID.fromString(params.getTargetWalletId()))
          .targetClientId(UUID.fromString(params.getOriginClientId()))
          .targetWalletId(UUID.fromString(params.getOriginWalletId()))
          .amount(BigDecimal.valueOf(params.getAmount()))
          .build();
      final Transaction transaction = transactionService
          .doTransaction(buildTransaction);
      return ResponseEntity.ok(TransactionResponse.builder()
          .transactionId(transaction.getId())
          .amount(transaction.getAmount())
          .build());
    } catch (IllegalArgumentException e) {
      logger.error("Error to findWalletsById: {}#{}. Message: {}",
          params.getOriginClientId(), params.getOriginWalletId(), e.getMessage());
      throw new ApplicationException(e);
    }
  }

  @Operation(summary = "Search all wallets of client identifier")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Return the list of the client wallets ",
          content = {
              @Content(mediaType = "application/json")
          }
      ),
      @ApiResponse(responseCode = "400", description = "business layer error when try find wallets",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "list of wallets not found",
          content = @Content(mediaType = "application/json"))
  })
  @GetMapping(path = WALLETS_BY_CLIENT)
  ResponseEntity<List<Wallet>> findAllWalletsByClient(@PathVariable final String clientId) {
    try {
      return ResponseEntity.ok(service.findAllWallets(UUID.fromString(clientId)));
    } catch (IllegalArgumentException e) {
      logger.error("Error to findAllWalletsByClient: client#{}. Message: {}", clientId, e.getMessage());
      throw new ApplicationException(e);
    }
  }

  @Operation(summary = "Search a client wallet")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Return the client wallet",
          content = {
              @Content(mediaType = "application/json")
          }
      ),
      @ApiResponse(responseCode = "400", description = "business layer error when try find wallet",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "wallet not found",
          content = @Content(mediaType = "application/json"))
  })
  @GetMapping(path = BY_WALLET_AND_CLIENT_ID)
  ResponseEntity<Wallet> findWalletsById(@PathVariable final String clientId, @PathVariable final String walletId) {
    try {
      return ResponseEntity.ok(service
          .findAWallet(UUID.fromString(clientId), UUID.fromString(walletId)));
    } catch (IllegalArgumentException e) {
      logger.error("Error to findWalletsById: wallet#{} - {}. Message: {}", walletId, clientId, e.getMessage());
      throw new ApplicationException(e);
    }
  }

  @Operation(summary = "Search a client by wallet identifier")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Return the client of the informed wallet",
          content = {
              @Content(mediaType = "application/json")
          }
      ),
      @ApiResponse(responseCode = "400", description = "business layer error when try find a client",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "client not found",
          content = @Content(mediaType = "application/json"))
  })
  @GetMapping(path = CLIENT_BY_WALLET_ID)
  ResponseEntity<Client> findClientByWalletId(@PathVariable final String walletId) {
    try {
      return ResponseEntity.ok(service.findClientByWalletId(UUID.fromString(walletId)));
    } catch (IllegalArgumentException e) {
      logger.error("Error to findClientByWalletId: wallet#{}. Message: {}", walletId, e.getMessage());
      throw new ApplicationException(e);
    }
  }

  @Operation(summary = "Found all transactions by wallet identifier")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Return all client wallet transactions",
          content = {
              @Content(mediaType = "application/json")
          }
      ),
      @ApiResponse(responseCode = "400", description = "business layer error when try find wallet transactions",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "wallet no has transactions",
          content = @Content(mediaType = "application/json"))
  })
  @GetMapping(path = CLIENT_WALLET_TRANSACTIONS)
  ResponseEntity<List<WalletTransactions>> findAllByWalletId(@PathVariable final String walletId) {
    try {
      return ResponseEntity.ok(transactionService
          .findAllByWalletId(UUID.fromString(walletId)));
    } catch (IllegalArgumentException e) {
      logger.error("Error to findAllByWalletId: wallet#{}. Message: {}", walletId, e.getMessage());
      throw new ApplicationException(e);
    }
  }
}
