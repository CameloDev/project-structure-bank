package capstoneds2.creditcard_module;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreditCardJavaFxApp extends Application {

    private static ConfigurableApplicationContext parentContext;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Configura o FXMLLoader para usar beans do Spring
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
        loader.setControllerFactory(parentContext::getBean); // Usa o contexto do módulo principal

        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 800);

        // Carrega CSS
        String css = getClass().getResource("/view/dashboard.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Cartão de Crédito - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setParentContext(ConfigurableApplicationContext context) {
        parentContext = context;
    }
}