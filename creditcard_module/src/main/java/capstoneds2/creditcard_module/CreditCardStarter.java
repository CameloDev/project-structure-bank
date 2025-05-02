package capstoneds2.creditcard_module;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CreditCardStarter implements ApplicationRunner {

    private final ConfigurableApplicationContext context;

    public CreditCardStarter(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Passa o contexto Spring para o CreditCardJavaFxApp
        CreditCardJavaFxApp.setParentContext(context);
    }
}