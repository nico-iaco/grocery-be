package it.iacovelli.grocerybe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( proxyBeanMethods = false )
public class GroceryBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryBeApplication.class, args);
    }

}
