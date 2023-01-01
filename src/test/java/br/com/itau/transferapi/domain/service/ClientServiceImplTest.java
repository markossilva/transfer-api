package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.ClientProvider;
import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientServiceImplTest {

    private ClientRepository clientRepository;
    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;

    private ClientService service;
    private UUID randomClientID;
    private UUID randomWalletID;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        walletRepository = mock(WalletRepository.class);
        randomClientID = UUID.randomUUID();
        randomWalletID = UUID.randomUUID();

        service = new ClientServiceImpl(clientRepository, transactionRepository, walletRepository);
    }

    @Test
    void shouldCreateClient_thenSaveIt() {
        final Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("ClientName")
                .build();

        final UUID id = service.createNewClient(client);

        verify(clientRepository).save(any(Client.class));
        assertNotNull(id);
    }

    @Test
    void shouldCreateWallet_inSavedClient() {
        final Client client = ClientProvider.getCreatedClient();
        final Wallet createdWallet = ClientProvider.getCreatedWallet(client.getId());
        when(clientRepository.findById(client.getId()))
                .thenReturn(Optional.of(client));
        when(walletRepository.save(createdWallet))
                .thenReturn(createdWallet);

        final Wallet newWallet = service.createNewWallet(client.getId(), createdWallet);

        verify(walletRepository).save(createdWallet);
        assertEquals(newWallet.getClientId(), client.getId());
    }

    @Test
    void shouldAddWallet_thenThrowException_noFindClient() {
        final Wallet createdWallet = ClientProvider.getCreatedWallet(UUID.randomUUID());
        when(clientRepository.findById(randomClientID))
                .thenReturn(Optional.empty());

        final Executable executable = () -> service.createNewWallet(randomClientID, createdWallet);

        verify(walletRepository, times(0))
                .save(any(Wallet.class));
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void shouldAddWallet_thenThrowException_differentClientIdFromWallet() {
        final Client client = ClientProvider.getCreatedClient();
        final Wallet createdWallet = ClientProvider.getCreatedWallet(UUID.randomUUID());
        when(clientRepository.findById(randomClientID)).thenReturn(Optional.of(client));

        final Executable executable = () -> service.createNewWallet(randomClientID, createdWallet);

        verify(walletRepository, times(0))
                .save(any(Wallet.class));
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void shouldFindAllWallets_thenFindClient() {
        List<Wallet> wallets = Arrays.asList(ClientProvider.getCreatedWallet(UUID.randomUUID()),
                ClientProvider.getCreatedWallet(UUID.randomUUID()));
        when(walletRepository.findByClientId(randomClientID))
                .thenReturn(Optional.of(wallets));

        final List<Wallet> savedWallets = service.findAllWallets(randomClientID);

        verify(walletRepository).findByClientId(randomClientID);
        assertTrue(savedWallets.containsAll(wallets));
    }

    @Test
    void shouldFindAllWallets_thenThrowException() {
        when(walletRepository.findByClientId(randomClientID))
                .thenReturn(Optional.empty());

        final Executable executable = () -> service.findAllWallets(randomClientID);

        verify(walletRepository, times(0))
                .findByClientId(any(UUID.class));
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void shouldFindAllTransactions_byClientId() {
        final List<Transaction> providedTransactions = ClientProvider
                .getCreatedTransactions(randomClientID, UUID.randomUUID());
        when(transactionRepository.findAllByClientId(randomClientID))
                .thenReturn(Optional.of(providedTransactions));

        final List<Transaction> allTransactions = service.findAllTransactions(randomClientID);

        verify(transactionRepository).findAllByClientId(randomClientID);
        assertTrue(allTransactions.containsAll(providedTransactions));
    }

    @Test
    void shouldFindAllTransactions_thenThrowException() {
        when(transactionRepository.findAllByClientId(randomClientID))
                .thenReturn(Optional.empty());

        final Executable executable = () -> service.findAllTransactions(randomClientID);

        verify(transactionRepository, times(0))
                .findAllByClientId(any(UUID.class));
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void shouldFindAllTransactions_byWallet() {
        final List<Transaction> providedTransactions = ClientProvider
                .getCreatedTransactions(randomClientID, randomWalletID);
        when(transactionRepository.findAllByClientIdAndWallet(randomClientID, randomWalletID))
                .thenReturn(Optional.of(providedTransactions));

        final List<Transaction> allTransactions = service
                .findAllTransactionsByWallet(randomClientID, randomWalletID);

        verify(transactionRepository).findAllByClientIdAndWallet(randomClientID, randomWalletID);
        assertTrue(allTransactions.containsAll(providedTransactions));
    }

    @Test
    void shouldFindAllTransactions_byWallet_thenThrowException() {
        when(transactionRepository.findAllByClientIdAndWallet(randomClientID, randomWalletID))
                .thenReturn(Optional.empty());

        final Executable executable = () -> service.findAllTransactionsByWallet(randomClientID, randomWalletID);

        verify(transactionRepository, times(0))
                .findAllByClientIdAndWallet(any(UUID.class), any(UUID.class));
        assertThrows(RuntimeException.class, executable);
    }
}
