package academiccalendar.ui.color;

import academiccalendar.service.ColorGroupService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
class AddColorController extends AbstractDraggableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddColorController.class);

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private ColorGroupService colorGroupService;


    @FXML
    private JFXTextField colorName;

    @FXML
    private JFXColorPicker colorPicker;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    @FXML
    private void exit(MouseEvent mouseEvent) {

    }

    @FXML
    private void cancel(MouseEvent mouseEvent) {

    }


    @FXML
    private void createNewColor(MouseEvent mouseEvent) {
        LOGGER.info("Create new color");
        String name = colorName.getText();
        Color colorFromLabel = colorPicker.getValue();
        colorGroupService.save(name, colorFromLabel);
        mainController.loadColors();

        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


}
