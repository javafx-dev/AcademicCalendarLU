package academiccalendar.ui.editterm;

import academiccalendar.data.model.Model;
import academiccalendar.service.TermService;
import academiccalendar.ui.listterms.ListTermsController;
import com.jfoenix.controls.JFXDatePicker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


@Component
public class EditTermController implements Initializable {

    @Autowired
    private TermService termService;

    @Autowired
    private ListTermsController listController;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label topLabel;
    @FXML
    private Label termLabel;
    @FXML
    private JFXDatePicker termDatePicker;

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;

    private void autofill() {

        // Retrieve term name
        String name = Model.getInstance().term_name;
        //Set name to TermLabel. show it ot the user
        termLabel.setText(name);

        //Retrieve date of start date selected term
        String termStartDate = Model.getInstance().term_date;
        String[] termDateParts = termStartDate.split("-");
        int year = Integer.parseInt(termDateParts[0]);
        int month = Integer.parseInt(termDateParts[1]);
        int day = Integer.parseInt(termDateParts[2]);

        // Set default value for datepicker
        termDatePicker.setValue(LocalDate.of(year, month, day));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        autofill();

        // ************* Everything below is for Draggable Window ********

        // Set up Mouse Dragging for the Event pop up window
        topLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });
        // Set up Mouse Dragging for the Event pop up window
        topLabel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });
        // Change cursor when hover over draggable area
        topLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.HAND); //Change cursor to hand
            }
        });

        // Change cursor when hover over draggable area
        topLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
            }
        });

    }

    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void update(MouseEvent event) {

        updateTermDate();

    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


    public void updateTermDate() {

        //Get the name of the term to be updated and it current starting date
        String termName = Model.getInstance().term_name;

        //Check if user actually selected a new starting date
        if (termDatePicker.getValue() == null) {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please select new starting date for the term");
            alertMessage.showAndWait();
            return;
        }
        termService.updateTerm(termName, termDatePicker.getValue());
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Term was updated successfully");
        alertMessage.showAndWait();

        // Update list of terms in the table view to show new starting date
        listController.loadData();
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }

}
