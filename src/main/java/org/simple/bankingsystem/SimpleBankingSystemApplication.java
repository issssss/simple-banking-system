package org.simple.bankingsystem;

import org.simple.bankingsystem.fileimport.generators.AccountFileGenerator;
import org.simple.bankingsystem.fileimport.generators.CustomerFileGenerator;
import org.simple.bankingsystem.fileimport.generators.TransactionFileGenerator;
import org.simple.bankingsystem.fileimport.parsers.AccountFileParser;
import org.simple.bankingsystem.fileimport.parsers.CustomerFileParser;
import org.simple.bankingsystem.fileimport.parsers.TransactionFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SimpleBankingSystemApplication {

    private final Logger log = LoggerFactory.getLogger(SimpleBankingSystemApplication.class);
    public static void main(String[] args) {

        SpringApplication.run(SimpleBankingSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAndParseFilesToDatabase(
            AccountFileGenerator accountFileGenerator,
            CustomerFileGenerator customerFileGenerator,
            TransactionFileGenerator transactionFileGenerator,
            AccountFileParser accountFileParser,
            CustomerFileParser customerFileParser,
            TransactionFileParser transactionFileParser) {
        return args -> {
            log.info("Starting files generation.");
            accountFileGenerator.generateFile();
            customerFileGenerator.generateFile();
            transactionFileGenerator.generateFile();
            log.info("Finished files generation.");

            log.info("Starting files parsing and mapping.");
            customerFileParser.parseFile("customers.csv");
            accountFileParser.parseFile("accounts.csv");
            transactionFileParser.parseFile("transactions.csv");
            log.info("Finished files parsing and mapping.");
        };
    }

}
