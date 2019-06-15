package com.ttulka.ddd.tlr;

import com.ttulka.ddd.tlr.domain.Accounts;
import com.ttulka.ddd.tlr.domain.Transactions;
import com.ttulka.ddd.tlr.infrastructure.AccountEntries;
import com.ttulka.ddd.tlr.infrastructure.TransactionEntries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import lombok.extern.slf4j.Slf4j;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(Config.class)
@Slf4j
public class Application implements CommandLineRunner {

    @Autowired
    private Demo demo;

    @Override
    public void run(String... args) {
        demo.run();
    }

    @Bean
    Demo demo(Accounts accounts, Transactions transactions, AccountEntries accountEntries, TransactionEntries transactionEntries) {
        return new Demo(accounts, transactions, accountEntries, transactionEntries);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
