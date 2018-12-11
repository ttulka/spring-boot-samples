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
    MyBeanA myBean1(MyBeanB myBeanB) {
        return new MyBeanA(myBeanB);
    }

    @Bean
    MyBeanB myBean2(MyBeanA myBeanA) {
        return new MyBeanB(myBeanA);
    }
}

class MyBeanA {

    private final MyBeanB myBeanB;

    MyBeanA(MyBeanB myBeanB) {
        this.myBeanB = myBeanB;
    }

    public void fnA1() {
        System.out.println("fnA1");
    }

    public void fnA2() {
        myBeanB.fnB1();
    }
}

class MyBeanB {

    private final MyBeanA myBeanA;

    MyBeanB(MyBeanA myBeanA) {
        this.myBeanA = myBeanA;
    }

    public void fnB1() {
        System.out.println("fnB1");
    }

    public void fnB2() {
        myBeanA.fnA1();
    }
}