package com.ttulkla.samples.transactions;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
class MyService {

    private final MyService2 myService2;
    private final JdbcTemplate jdbc;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void doSERIALIZABLE() {
        try {
            System.out.println("SERIALIZABLE begin: " + jdbc.queryForList("SELECT * FROM mytest"));
            Thread.sleep(500);
            System.out.println("SERIALIZABLE end: " + jdbc.queryForList("SELECT * FROM mytest"));

            //myService2.doNEVER();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public int doUNCOMMITTED(int i) {
        try {
            System.out.println("UNCOMMITTED " + i + " begin: " + jdbc.queryForObject("SELECT COUNT(*) FROM mytest", Integer.class));
            jdbc.update("INSERT INTO mytest VALUES (?)", new Object[]{i});
            System.out.println("UNCOMMITTED " + i + " end: " + jdbc.queryForObject("SELECT COUNT(*) FROM mytest", Integer.class));
            if (true) {
                //throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
