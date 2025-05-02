package capstoneds2.creditcard_module;

import javafx.application.Application;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class CreditcardModuleApplication {

    // Spring context compartilhado entre o Spring Boot e JavaFX
    public static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Inicializa o Spring Boot e obtém o contexto
        springContext = new SpringApplicationBuilder(CreditcardModuleApplication.class)
                .run(args); // Inicia o contexto Spring

        // Inicia o JavaFX
        Application.launch(CreditCardJavaFxApp.class, args);
    }
}