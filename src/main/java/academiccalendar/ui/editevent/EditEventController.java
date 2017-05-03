package academiccalendar.ui.editevent;

import academiccalendar.model.DbEvent;
import academiccalendar.model.DbTerm;
import academiccalendar.service.EventService;
import academiccalendar.service.TermService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.main.FXMLDocumentController;
import academiccalendar.utils.DateConverter;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class EditEventController extends AbstractDraggableController {

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private TermService termService;

    @Autowired
    private EventService eventService;

    private DbEvent event;

    @FXML
    private JFXTextField subject;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXDatePicker date;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> termsList = termService.getListOfTerms();
        termSelect.setItems(termsList);
        super.initialize(url, rb);
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
        event.setDate(DateConverter.localDateToDate(date.getValue()));
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
        autoFillDatePicker();
    }

    private void autoFillDatePicker() {
        date.setValue(DateConverter.dateToLocalDate((java.sql.Date)event.getDate()));
        subject.setText(event.getDescription());
        termSelect.getSelectionModel().select(event.getTerm().getName());
    }

    public DbEvent getEvent() {
        return event;
    }
}
