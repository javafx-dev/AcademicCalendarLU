package academiccalendar.ui.editevent;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbEvent;
import academiccalendar.model.DbTerm;
import academiccalendar.service.EventService;
import academiccalendar.service.TermService;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class EditEventController implements Initializable {

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private TermService termService;

    @Autowired
    private EventService eventService;

    private DbEvent event;

    @FXML
    private Label topLabel;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXDatePicker date;
    @FXML
    private AnchorPane rootPane;

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;


    private void autofillDatePicker() {
        // Get selected day, month, and year and autofill date selection
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        int termID = Model.getInstance().event_term_id;
        String descript = Model.getInstance().event_subject;

        DbTerm dbTerm = termService.findById((long) termID);

        String chosenTermName = dbTerm.getName();

        // Set default value for datepicker
        date.setValue(LocalDate.of(year, month, day));

        // Fill description field
        subject.setText(descript);

        termSelect.getSelectionModel().select(chosenTermName);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        autofillDatePicker();


        ObservableList<String> termsList = termService.getListOfTerms();
        termSelect.setItems(termsList);

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
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void update(MouseEvent event) {
        updateEvent();
    }

    @FXML
    private void delete(MouseEvent event) {

        //Show confirmation dialog to make sure the user want to delete the selected event
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Event Deletion");
        alert.setContentText("Are you sure you want to delete this event?");
        //Customize the buttons in the confirmation dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        //Set buttons onto the confirmation window
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        //Get the user's answer on whether deleting or not
        Optional<ButtonType> result = alert.showAndWait();

        //If the user wants to delete the event, call the function that deletes the event. Otherwise, close the window
        if (result.get() == buttonTypeYes) {
            deleteEvent();
        } else {
            // Close the window
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }

    }


    private void updateEvent() {
        // Subject for the event
        String newEventSubject = subject.getText();
        // Get term that was selected by the user
        String term = termSelect.getValue();

        DbTerm dbTerm = termService.findByName(term);
        event.setDescription(newEventSubject);
        event.setDate(date.getValue());
        event.setTerm(dbTerm);
        eventService.save(event);

        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Event was updated successfully");
        alertMessage.showAndWait();

        // Update view
        mainController.repaintView();

        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }


    private void deleteEvent() {
        eventService.delete(event);

        //Show message indicating that the selected rule was deleted
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Selected event was successfully deleted");
        alertMessage.showAndWait();

        // Update view
        mainController.repaintView();

        // Close the window, so that when user clicks on "Manage Rules" only the remaining existing rules appear
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void setEvent(DbEvent event) {
        this.event = event;
    }

    public DbEvent getEvent() {
        return event;
    }
}
