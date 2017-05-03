package academiccalendar.ui.addcalendar;

import academiccalendar.model.DbCalendar;
import academiccalendar.service.CalendarService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

@Component
class AddCalendarController extends AbstractDraggableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddCalendarController.class);

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private CalendarService calendarService;

    @FXML
    private JFXTextField calendarName;
    @FXML
    private JFXDatePicker date;


    @FXML
    void generateNewCalendar(MouseEvent event) throws ParseException {

        if ((date.getValue() != null) && (!calendarName.getText().isEmpty())) {

            // Define date format
            DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Get the date value from the date picker
            String startingDate = date.getValue().format(myFormat);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = df.parse(startingDate);

            String calName = calendarName.getText();

            DbCalendar dbCalendar = new DbCalendar();
            dbCalendar.setName(calName);
            dbCalendar.setStartYear(2017);
            dbCalendar.setEndYear(2018);
            dbCalendar.setStartDate(startDate);
            calendarService.save(dbCalendar);

            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Calendar was created successfully");
            alertMessage.showAndWait();

            // Load the calendar in the main window
            mainController.calendarGenerate(dbCalendar);

            // Close the window
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } else {
            LOGGER.info("Error: user did not fill out all fields");

            Alert alert = new Alert(AlertType.WARNING, "Please fill out all fields.");
            alert.showAndWait();
        }
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
    }

    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}
