package com.ttulkla.samples.transactions;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.SneakyThrows;

@SpringBootApplication
public class ConcurrentTransactionsApplication {

    @SneakyThrows
    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(ConcurrentTransactionsApplication.class, args);

        var jdbc = ac.getBean(JdbcTemplate.class);

        var service = ac.getBean(MyService.class);
        var executor = Executors.newFixedThreadPool(5);

        var tasks = new ArrayList<Callable<Integer>>();
        for (int i = 0; i < 5; i++) {
            final var idx = i + 1;
            tasks.add(() -> service.doUNCOMMITTED(idx));
        }

        new Thread(service::doSERIALIZABLE).start();

        executor.invokeAll(tasks);
        executor.shutdown();

        Thread.sleep(500);

        System.out.println("FINISHED: " + jdbc.queryForObject("SELECT COUNT(*) FROM mytest", Integer.class));
    }

}
