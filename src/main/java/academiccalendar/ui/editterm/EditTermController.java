package academiccalendar.ui.editterm;

import academiccalendar.data.model.Model;
import academiccalendar.service.TermService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.listterms.ListTermsController;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


@Component
public class EditTermController extends AbstractDraggableController {

    @Autowired
    private TermService termService;

    @Autowired
    private ListTermsController listController;
    @FXML
    private Label termLabel;
    @FXML
    private JFXDatePicker termDatePicker;

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
        super.initialize(url, rb);
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
