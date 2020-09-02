package com.ttulkla.samples.transactions;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MyService2 {

    @Transactional(propagation = Propagation.NEVER)
    public void doNEVER() {
    }
}
