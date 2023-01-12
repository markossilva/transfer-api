package br.com.itau.transferapi.application.rest;

import br.com.itau.transferapi.Application;
import br.com.itau.transferapi.application.req.CreateClientParams;
import br.com.itau.transferapi.application.req.TransactionParams;
import br.com.itau.transferapi.domain.model.*;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = Application.class
)
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties"
)
public class ClientControllerIntegrationTest {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private ObjectMapper mapper;
  @MockBean
  private ClientRepository clientRepository;
  @MockBean
  private WalletRepository walletRepository;
  @MockBean
  private TransactionRepository transactionRepository;
  private MockMvc mockMvc;

  @Before
  public void setUp() {
    mapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build();
  }

  @Test
  public void givenClients_whenGetAll_thenStatus200() throws Exception {
    when(clientRepository.findAll())
        .thenReturn(Collections.singletonList(Client.builder()
            .id(UUID.randomUUID())
            .name("userTest")
            .build()));

    mockMvc.perform(get("/v1/client/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name", is("userTest")));
  }

  @Test
  public void givenIds_whenCreateClient_thenStatus200() throws Exception {
    CreateClientParams params = new CreateClientParams("userTest", 10.0);
    final String jsonParams = mapper.writeValueAsString(params);

    mockMvc.perform(post("/v1/client/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonParams)
        )
        .andExpect(status().isCreated())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  public void givenTransactionId_whenTransferAmount_thenStatus200() throws Exception {
    final UUID targetWalletId = UUID.randomUUID();
    final UUID targetClientId = UUID.randomUUID();

    final UUID originWalletId = UUID.randomUUID();
    final UUID originClientId = UUID.randomUUID();

    TransactionParams params = new TransactionParams(
        originClientId.toString(),
        originWalletId.toString(),
        targetClientId.toString(),
        targetWalletId.toString(),
        1.0
    );
    final String jsonParams = mapper.writeValueAsString(params);

    when(walletRepository.findById(targetClientId, targetWalletId))
        .thenReturn(Optional.of(Wallet.builder()
            .clientId(targetClientId)
            .id(targetWalletId)
            .status(WalletStatus.ACTIVE)
            .balance(BigDecimal.TEN)
            .build()));
    when(walletRepository.findById(originClientId, originWalletId))
        .thenReturn(Optional.of(Wallet.builder()
            .clientId(originClientId)
            .id(targetWalletId)
            .status(WalletStatus.ACTIVE)
            .balance(BigDecimal.TEN)
            .build()));

    when(transactionRepository.save(any(Transaction.class)))
        .thenReturn(Transaction.builder()
            .id(BigInteger.ONE)
            .clientId(targetClientId)
            .walletId(targetWalletId)
            .targetClientId(originClientId)
            .targetWalletId(originWalletId)
            .amount(BigDecimal.ONE)
            .build());

    mockMvc.perform(put("/v1/client/transfer/amount")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonParams))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.transactionId", is(1)))
        .andExpect(jsonPath("$.amount", is(1)));
  }

  @Test
  public void givenWallets_whenGetAllById_thenStatus200() throws Exception {
    final UUID clientId = UUID.randomUUID();

    when(walletRepository.findByClientId(clientId))
        .thenReturn(Optional.of(Collections
            .singletonList(Wallet.builder()
                .clientId(clientId)
                .id(UUID.randomUUID())
                .status(WalletStatus.ACTIVE)
                .balance(BigDecimal.TEN)
                .build())));

    mockMvc.perform(get(String.format("/v1/client/%s/wallets", clientId))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].clientId", is(clientId.toString())));
  }

  @Test
  public void givenWallet_whenGetByWalletAndClientId_thenStatus200() throws Exception {
    final UUID clientId = UUID.randomUUID();
    final UUID walletId = UUID.randomUUID();

    when(walletRepository.findById(clientId, walletId))
        .thenReturn(Optional.of(Wallet.builder()
            .clientId(clientId)
            .id(UUID.randomUUID())
            .status(WalletStatus.ACTIVE)
            .balance(BigDecimal.TEN)
            .build()));

    mockMvc.perform(get(String.format("/v1/client/%s/wallet/%s", clientId, walletId))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.clientId", is(clientId.toString())));
  }

  @Test
  public void givenClient_whenGetByWalletId_thenStatus200() throws Exception {
    final UUID walletId = UUID.randomUUID();
    final UUID clientId = UUID.randomUUID();

    when(clientRepository.findClientByWalletId(walletId))
        .thenReturn(Optional.of(Client.builder()
            .id(clientId)
            .name("userTest")
            .build()));

    mockMvc.perform(get(String.format("/v1/client/wallet/%s", walletId))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(clientId.toString())))
        .andExpect(jsonPath("$.name", is("userTest")));
  }

  @Test
  public void givenAllTransactions_whenGetByWalletId_thenStatus200() throws Exception {
    final UUID walletId = UUID.randomUUID();

    final WalletTransactions walletTransactions =
        new WalletTransactions(
            BigDecimal.TEN,
            "",
            TransactionType.SEND,
            "",
            LocalDateTime.now());
    when(transactionRepository.findAllByWalletId(walletId))
        .thenReturn(Optional.of(Collections.singletonList(walletTransactions)));

    mockMvc.perform(get(String.format("/v1/client/wallet/transactions/%s", walletId))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].amount", is(10)));
  }
}
