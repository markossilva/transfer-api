package br.com.itau.transferapi.domain.service;

import br.com.itau.transferapi.domain.model.Transaction;

import java.math.BigInteger;

public interface TransactionService {
  BigInteger doTransaction(Transaction transaction);
}
