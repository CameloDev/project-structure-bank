package capstoneds2.creditcard_module;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class CreditCardJavaFxApp extends Application {

    @Setter
    private static ConfigurableApplicationContext parentContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega o FXML
        try (InputStream fxmlStream = getClass().getResourceAsStream("/view/Dashboard.fxml")) {
            if (fxmlStream == null) {
                throw new FileNotFoundException("FXML file not found.");
            }
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(fxmlStream);
            Scene scene = new Scene(root, 300, 250);
            primaryStage.setTitle("Hello World!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("FXML file not found in resources.");
        }
    }

    public static void setParentContext(ConfigurableApplicationContext context) {
        parentContext = context;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


