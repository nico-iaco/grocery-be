package it.iacovelli.grocerybe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( proxyBeanMethods = false )
@OpenAPIDefinition(info = @Info(title = "Grocery API", version = "1.0.0",
        description = "Api of an app to avoid food waste"),
        servers = { @Server(url = "/", description = "Default URL")})
public class GroceryBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryBeApplication.class, args);
    }

}
