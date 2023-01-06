package br.com.itau.transferapi.infrastracture.configuration;

import br.com.itau.transferapi.Application;
import br.com.itau.transferapi.domain.repository.ClientRepository;
import br.com.itau.transferapi.domain.repository.TransactionRepository;
import br.com.itau.transferapi.domain.repository.WalletRepository;
import br.com.itau.transferapi.domain.service.ClientService;
import br.com.itau.transferapi.domain.service.TransactionService;
import br.com.itau.transferapi.domain.service.impl.ClientServiceImpl;
import br.com.itau.transferapi.domain.service.impl.TransactionServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan(basePackageClasses = Application.class)
public class BeanConfiguration {

  @Bean
  TransactionService transactionService(final WalletRepository walletRepository,
                                        final TransactionRepository transactionRepository) {
    return new TransactionServiceImpl(transactionRepository, walletRepository);
  }

  @Bean
  ClientService clientService(final ClientRepository clientRepository,
                              final WalletRepository walletRepository) {
    return new ClientServiceImpl(clientRepository, walletRepository);
  }

  @Bean
  ModelMapper mapper() {
    return new ModelMapper();
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build();
  }
}
