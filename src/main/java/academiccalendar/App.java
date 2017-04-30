package academiccalendar;

import academiccalendar.ui.main.AbstractJavaFxApplicationSupport;
import academiccalendar.ui.main.CustomFXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import java.net.URL;

@Lazy
@SpringBootApplication
@ComponentScan(basePackages = "academiccalendar")
@EnableAutoConfiguration
public class App extends AbstractJavaFxApplicationSupport {

    @Value("${app.ui.title}")
    private String windowTitle;

    @Autowired
    private CustomFXMLLoader customFXMLLoader;

    @Override
    public void start(Stage stage) throws Exception {

        URL res = getClass().getClassLoader().getResource("FXMLDocument.fxml");

        Parent root = customFXMLLoader.load(res);

        // Set main window icon
        stage.getIcons().add(
                new Image("icons/app_icon.png"));

        // Maximize window at launch
        stage.setMaximized(true);
        stage.setTitle(windowTitle);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launchApp(App.class, args);
    }

}
