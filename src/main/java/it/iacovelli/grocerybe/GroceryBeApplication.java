package it.iacovelli.grocerybe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class GroceryBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryBeApplication.class, args);
    }

}
