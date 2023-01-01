package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.model.Client;
import br.com.itau.transferapi.domain.model.Transaction;
import br.com.itau.transferapi.domain.model.Wallet;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    UUID createNewClient(final Client client);

    Wallet createNewWallet(final UUID customerID, final Wallet wallet);

    List<Wallet> findAllWallets(final UUID clientID);

    List<Transaction> findAllTransactions(final UUID clientID);

    List<Transaction> findAllTransactionsByWallet(final UUID clientID, final UUID walletID);
}
