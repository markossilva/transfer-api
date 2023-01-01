package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.exception.DomainException;
import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.Wallet;
import br.com.itau.transferapi.domain.model.WalletStatus;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private static final String CLIENT_HAS_EXISTS = "Client with given id doesn't exist";
    private static final String CLIENT_HAS_NO_WALLETS = "Client with given id don't have any wallet";
    private static final String CLIENT_HAS_NO_TRANSACTIONS = "Client with given id don't have any transaction";
    private static final BigDecimal INITIAL_WALLET_BALANCE = BigDecimal.ZERO;

    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Override
    public UUID createNewClient(final Client client) {
        final Wallet clientWallet = Wallet.builder(client.getId(), UUID.randomUUID())
                .balance(INITIAL_WALLET_BALANCE)
                .status(WalletStatus.CREATED)
                .build();

        client.setWallet(Collections.singletonList(clientWallet));

        clientRepository.save(client);

        return client.getId();
    }

    @Override
    public UUID createNewWallet(final UUID clientId) {
        final Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException(CLIENT_HAS_EXISTS));

        final Wallet newWallet = Wallet.builder(client.getId(), UUID.randomUUID())
                .status(WalletStatus.CREATED)
                .balance(INITIAL_WALLET_BALANCE)
                .build();

        final Wallet createdWallet = walletRepository.save(newWallet);

        return createdWallet.getId();
    }

    @Override
    public List<Wallet> findAllWallets(UUID clientID) {
        return walletRepository.findByClientId(clientID)
                .orElseThrow(() -> new DomainException(CLIENT_HAS_NO_WALLETS));
    }

    @Override
    public List<Transaction> findAllTransactions(UUID clientID) {
        return transactionRepository.findAllByClientId(clientID)
                .orElseThrow(() -> new DomainException(CLIENT_HAS_NO_TRANSACTIONS));
    }

    @Override
    public List<Transaction> findAllTransactionsByWallet(UUID clientID, UUID walletID) {
        return transactionRepository.findAllByClientIdAndWallet(clientID, walletID)
                .orElseThrow(() -> new DomainException(CLIENT_HAS_NO_TRANSACTIONS));
    }
}
