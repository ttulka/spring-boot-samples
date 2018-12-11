package cz.net21.ttulka.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    MyBeanA myBeanA(B1 b1) {
        return new MyBeanA(b1);
    }

    @Bean
    MyBeanB myBeanB(A1 a1) {
        return new MyBeanB(a1);
    }
}

interface A1 {
    void fnA1();
}

interface B1 {
    void fnB1();
}

class MyBeanA implements A1 {

    private final B1 b1;

    MyBeanA(B1 b1) {
        this.b1 = b1;
    }

    public void fnA1() {
        System.out.println("fnA1");
    }

    public void fnA2() {
        b1.fnB1();
    }
}

class MyBeanB implements B1 {

    private final A1 a1;

    MyBeanB(A1 a1) {
        this.a1 = a1;
    }

    public void fnB1() {
        System.out.println("fnB1");
    }

    public void fnB2() {
        a1.fnA1();
    }
}