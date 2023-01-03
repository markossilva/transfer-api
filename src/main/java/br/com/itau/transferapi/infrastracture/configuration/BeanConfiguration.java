package br.com.itau.transferapi.infrastracture.configuration;

import br.com.itau.transferapi.Application;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.domain.service.ClientService;
import br.com.itau.transferapi.domain.service.ClientServiceImpl;
import br.com.itau.transferapi.domain.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Application.class)
public class BeanConfiguration {

  @Bean
  TransactionService transactionService(final WalletRepository walletRepository,
                                        final TransactionRepository transactionRepository) {
    return new ClientServiceImpl(transactionRepository, walletRepository);
  }

  @Bean
  ClientService clientService(final ClientRepository clientRepository,
                              final WalletRepository walletRepository,
                              final TransactionRepository transactionRepository) {
    return new ClientServiceImpl(clientRepository, transactionRepository, walletRepository);
  }
}