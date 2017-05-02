package academiccalendar.ui.addrule;

import academiccalendar.model.DbRule;
import academiccalendar.model.DbTerm;
import academiccalendar.service.RuleService;
import academiccalendar.service.TermService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AddRuleController extends AbstractDraggableController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private TermService termService;

    @FXML
    private JFXTextField eventDescript;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXTextField daysFromStart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> terms = termService.getListOfTerms();
        termSelect.setItems(terms);
        super.initialize(url, rb);
    }

    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(MouseEvent event) {


        if (eventDescript.getText().isEmpty() || termSelect.getSelectionModel().isEmpty()
                || daysFromStart.getText().isEmpty()) {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please fill out all fields");
            alertMessage.showAndWait();
            return;
        }

        // Get fields for rule
        String eventDescription = eventDescript.getText();
        int days = Integer.parseInt(daysFromStart.getText());
        String term = termSelect.getValue();


        //*********************************************************************
        //Save rule into the database
        saveRuleInDatabase(eventDescription, term, days);
        //*********************************************************************

        // Close the stage
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


    private void saveRuleInDatabase(String eventDescription, String termName, int daysFromStart) {
        //Get term ID for the term selected because it is needed to save the rule in the RULES table due int attribute called TermID
        DbTerm term = termService.findByName(termName);

        DbRule rule = new DbRule();
        rule.setTerm(term);
        rule.setDayFromStart(daysFromStart);
        rule.setDescription(eventDescription);
        ruleService.save(rule);

        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Rule was added successfully");
        alertMessage.showAndWait();

    }

}
