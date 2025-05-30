package capstoneds2.main_app;

import capstoneds2.creditcard_module.CreditCardJavaFxApp;
import capstoneds2.creditcard_module.CreditCardModuleConfig;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {
        "capstoneds2.account_module",
        "capstoneds2.creditcard_module",
        "capstoneds2.transaction_module",
        "capstoneds2.external_payment_module",
        "capstoneds2.commons_module",
        "capstoneds2.main_app"
})
@Import(CreditCardModuleConfig.class)
@EnableTransactionManagement
public class MainAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainAppApplication.class, args);

        String[] activeProfiles = context.getEnvironment().getActiveProfiles();

        boolean isTestProfile = Arrays.asList(activeProfiles).contains("test");

        if (!isTestProfile) {
            String activeModule = context.getEnvironment().getProperty("app.module.active", "none");

            if ("creditcard".equals(activeModule)) {
                CreditCardJavaFxApp.setParentContext(context);
                Application.launch(CreditCardJavaFxApp.class, args);
            }
        }
    }

}