package academiccalendar.ui.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class CustomFXMLLoader{

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    public Parent load(URL res) throws IOException {
        return FXMLLoader.load(res, null, null, applicationContext::getBean);
    }
}
