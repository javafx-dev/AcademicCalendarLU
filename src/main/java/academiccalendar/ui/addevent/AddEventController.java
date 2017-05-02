package academiccalendar.ui.addevent;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbCalendar;
import academiccalendar.model.DbEvent;
import academiccalendar.model.DbTerm;
import academiccalendar.service.CalendarService;
import academiccalendar.service.EventService;
import academiccalendar.service.TermService;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
public class AddEventController implements Initializable {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AddEventController.class);

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private EventService eventService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private TermService termService;

    // Structure
    @FXML
    private Label topLabel;
    @FXML
    private AnchorPane rootPane;

    // Text fields
    @FXML
    private JFXTextField subject;

    @FXML
    private JFXComboBox<String> termSelect;

    // Buttons
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    // Date picker
    @FXML
    private JFXDatePicker date;

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;

    @FXML
    void exit(MouseEvent event) {
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancel(MouseEvent event) {
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(MouseEvent event) {
        LOGGER.info("Save new event");
        // Get the calendar name
        Long calendarId = Model.getInstance().calendar_id;

        if (subject.getText().isEmpty() || termSelect.getSelectionModel().isEmpty()
                || date.getValue() == null) {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please fill out all fields");
            alertMessage.showAndWait();
            return;
        }


        // Subject for the event
        String eventSubject = subject.getText();

        // Get term that was selected by the user
        String term = termSelect.getValue();

        DbTerm dbTerm = termService.findByName(term);
        DbCalendar calendar = calendarService.findOne(calendarId);

        DbEvent dbEvent = new DbEvent();
        dbEvent.setDescription(eventSubject);
        dbEvent.setDate(date.getValue());
        dbEvent.setCalendar(calendar);
        dbEvent.setTerm(dbTerm);
        eventService.save(dbEvent);

        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Event was added successfully");
        alertMessage.showAndWait();

        //Show event on the calendar
        mainController.showDate(date.getValue().getDayOfMonth(), eventSubject, dbTerm.getId().intValue(), dbEvent);

        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void autofillDatePicker() {
        // Get selected day, month, and year and autofill date selection
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;

        // Set default value for datepicker
        date.setValue(LocalDate.of(year, month, day));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        autofillDatePicker();
        //Get terms from database and store them in the ObservableList variable "terms"

        ObservableList<String> terms = termService.getListOfTerms();
        termSelect.setItems(terms);
        //**********************************************************************
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

}
