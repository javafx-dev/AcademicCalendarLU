package academiccalendar.ui.editrule;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbRule;
import academiccalendar.model.DbTerm;
import academiccalendar.service.RuleService;
import academiccalendar.service.TermService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.listrules.ListRulesController;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXButton;
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
public class EditRuleController extends AbstractDraggableController {

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private ListRulesController listController;

    @Autowired
    private TermService termService;

    @Autowired
    private RuleService ruleService;

    private DbRule dbRule;

    @FXML
    private JFXTextField eventDescript;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXTextField daysFromStart;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private void autofill() {

        // Retrive rule data
        String days = Integer.toString(Model.getInstance().rule_days);
        String descript = Model.getInstance().rule_descript;
        String term = Model.getInstance().rule_term;

        // Show current data
        eventDescript.setText(descript);
        daysFromStart.setText(days);
        termSelect.getSelectionModel().select(term);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Get the list of exisitng terms from the database and show them in the correspondent drop-down menu
        ObservableList<String> termsList = termService.getListOfTerms();
        termSelect.setItems(termsList);


        // Auto fill values for rule fields
        autofill();
        super.initialize(url, rb);
    }

    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(MouseEvent event) {
        updateRule();
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void updateRule() {
        String newDescript = eventDescript.getText();
        String newTerm = termSelect.getValue();
        DbTerm newDbTerm = termService.findByName(newTerm);

        dbRule.setDescription(newDescript);
        dbRule.setDayFromStart(Integer.parseInt(daysFromStart.getText()));
        dbRule.setTerm(newDbTerm);
        ruleService.update(dbRule);

        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Rule was updated successfully");
        alertMessage.showAndWait();

        listController.loadData();

        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void setDbRule(DbRule dbRule) {
        this.dbRule = dbRule;
    }
}
