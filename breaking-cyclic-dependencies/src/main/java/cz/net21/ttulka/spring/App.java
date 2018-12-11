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
    MyBean1 myBean1(MyBean2 myBean2) {
        return new MyBean1(myBean2);
    }

    @Bean
    MyBean2 myBean2(MyBean1 myBean1) {
        return new MyBean2(myBean1);
    }
}

class MyBean1 {

    private final MyBean2 myBean2;

    MyBean1(MyBean2 myBean2) {
        this.myBean2 = myBean2;
    }

    public void fn1a() {
        System.out.println("fn1a");
    }

    public void fn1b() {
        myBean2.fn2a();
    }
}

class MyBean2 {

    private final MyBean1 myBean1;

    MyBean2(MyBean1 myBean1) {
        this.myBean1 = myBean1;
    }

    public void fn2a() {
        System.out.println("fn2a");
    }

    public void fn2b() {
        myBean1.fn1a();
    }
}