package capstoneds2.main_app;

import capstoneds2.creditcard_module.CreditCardJavaFxApp;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "capstoneds2.account_module",
        "capstoneds2.creditcard_module",
        "capstoneds2.transaction_module",
        "capstoneds2.external_payment_module",
        "capstoneds2.commons_module",
        "capstoneds2.main_app"
})
public class MainAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainAppApplication.class, args);

        String activeModule = context.getEnvironment().getProperty("app.module.active", "none");

        if ("creditcard".equals(activeModule)) {

            CreditCardJavaFxApp.setParentContext(context);

            Application.launch(CreditCardJavaFxApp.class, args);
        }
    }
}