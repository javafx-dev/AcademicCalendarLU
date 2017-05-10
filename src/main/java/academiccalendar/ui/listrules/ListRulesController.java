package academiccalendar.ui.listrules;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbCalendar;
import academiccalendar.model.DbEvent;
import academiccalendar.model.DbRule;
import academiccalendar.model.DbTerm;
import academiccalendar.service.CalendarService;
import academiccalendar.service.EventService;
import academiccalendar.service.RuleService;
import academiccalendar.service.TermService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.editrule.EditRuleController;
import academiccalendar.ui.main.CustomFXMLLoader;
import academiccalendar.ui.main.FXMLDocumentController;
import academiccalendar.utils.DateConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ListRulesController extends AbstractDraggableController {

    @FXML
    private TableView<academiccalendar.ui.main.Rule> tableView;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> eventCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> termCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> daysCol;

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private EditRuleController editEventController;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private EventService eventService;

    @Autowired
    private TermService termService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CustomFXMLLoader loader;

    public void initCol() {
        eventCol.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));
        termCol.setCellValueFactory(new PropertyValueFactory<>("termID"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("daysFromStart"));
    }

    public void loadData() {
        ObservableList<academiccalendar.ui.main.Rule> list = FXCollections.observableArrayList();
        // wipe current rules to add updates rules from database
        tableView.getItems().clear();

        List<DbRule> allRules = ruleService.getAllRules();
        for (DbRule ru : allRules) {
            list.add(new academiccalendar.ui.main.Rule(ru.getDescription(), ru.getTerm().getName(), Integer.toString(ru.getDayFromStart()), ru.getId()));
        }
        tableView.getItems().setAll(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
        super.initialize(url, rb);
    }

    @FXML
    private void exit(MouseEvent event) {
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addSelectedRule(MouseEvent event) {

        if (!tableView.getSelectionModel().isEmpty()) {
            // Get the information of selected rule on the table view
            academiccalendar.ui.main.Rule rule = tableView.getSelectionModel().getSelectedItem();
            String eventSubject = rule.getEventDescription();
            String auxTermName = rule.getTermID();
            DbTerm dbTerm = termService.findByName(auxTermName);

            // Get the calendar name
            Long calendarId = Model.getInstance().calendarId;

            //Create event based on selected rule
            boolean eventWasCreated = createEventFromRule(eventSubject, dbTerm, Integer.valueOf(rule.getDaysFromStart()), calendarId);

            //Check if event was create and show the correspondent message to the user
            if (eventWasCreated) {
                Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
                alertMessage.setHeaderText(null);
                alertMessage.setContentText("Event was created based on a rule successfully");
                alertMessage.showAndWait();
            } else {
                Alert alertMessage = new Alert(Alert.AlertType.ERROR);
                alertMessage.setHeaderText(null);
                alertMessage.setContentText("Creating event based on a rule failed!");
                alertMessage.showAndWait();
            }
        }

    }

    @FXML
    private void addAllRules(MouseEvent event) {

        // Get the calendar name
        Long calendarId = Model.getInstance().calendarId;

        //Get list of rules from database and store it in an ArrayList variable
        List<DbRule> listOfRules = ruleService.getAllRules();

        //Variable that keeps track of the number of events created
        int auxEventsCreated = 0;

        //Check if array list of rules is not empty
        if (!listOfRules.isEmpty()) {
            //Loop that creates all events based on rules
            for (DbRule rule : listOfRules) {
                //Splits rule by EventDescription, TermID, and DaysFromStart

                //Create event based on this rule
                boolean eventWasCreated = createEventFromRule(rule.getDescription(), rule.getTerm(), rule.getDayFromStart(), calendarId);

                //Increase number of events created
                if (eventWasCreated) {
                    auxEventsCreated++;
                }
            }

            if (auxEventsCreated > 0) {
                //Show message letting the user know that events were created successfully based on all rules
                Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
                alertMessage.setHeaderText(null);
                alertMessage.setContentText(auxEventsCreated + " Events Were Successfully Created Based On All Rules!");
                alertMessage.showAndWait();
            }
        } else {
            //Show message letting the user know that events were created successfully based on all rules
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Action Failed! There aren't any saved rules!");
            alertMessage.showAndWait();
        }
    }


    @FXML
    private void editRule(MouseEvent event) {
        editRuleEvent();
    }


    @FXML
    private void deleteRule(MouseEvent event) {

        if (!tableView.getSelectionModel().isEmpty()) {
            //Show confirmation dialog to make sure the user want to delete the selected rule
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Rule Deletion");
            alert.setContentText("Are you sure you want to delete this rule?");
            //Customize the buttons in the confirmation dialog
            ButtonType buttonTypeYes = ButtonType.YES;
            ButtonType buttonTypeNo = ButtonType.NO;
            //Set buttons onto the confirmation dialog
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            //Get the user's answer on whether deleting or not
            Optional<ButtonType> result = alert.showAndWait();

            //If the user wants to delete the rule, call the function that deletes the rule. Otherwise, close the window
            if (result.isPresent() && result.get() == buttonTypeYes) {
                deleteSelectedRule();
            }

        }

    }

    private void editRuleEvent() {

        if (!tableView.getSelectionModel().isEmpty()) {
            // Get selected rule data
            academiccalendar.ui.main.Rule rule = tableView.getSelectionModel().getSelectedItem();

            // Store rule data
            Model.getInstance().ruleTerm = rule.getTermID();
            Model.getInstance().ruleDescript = rule.getEventDescription();
            Model.getInstance().ruleDays = Integer.parseInt(rule.getDaysFromStart());

            // When user clicks on any date in the calendar, event editor window opens
            try {
                Long ruleId = rule.getDbRuleId();
                Parent rootLayout = loader.load(getClass().getClassLoader().getResource("edit_rule.fxml"));
                DbRule dbRule = ruleService.findById(ruleId);
                editEventController.setDbRule(dbRule);
                Stage stage = new Stage(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);


                // Show the scene containing the root layout.
                Scene scene = new Scene(rootLayout);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void deleteSelectedRule() {

        // Get the information of selected rule on the table view
        academiccalendar.ui.main.Rule rule = tableView.getSelectionModel().getSelectedItem();

        ruleService.delete(rule.getDbRuleId());

        //Show message indicating that the selected rule was deleted
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Selected rule was successfully deleted");
        alertMessage.showAndWait();


        // Close the window, so that when user clicks on "Manage Rules" only the remaining existing rules appear
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }

    //**************************************************************************************************************************
    //************************    Auxiliary Functions For Creating Events Based On Rules    ************************************
    //**************************************************************************************************************************

    //Function that creates event based on rule and inserts it into the database in the EVENTS table
    //and return a boolean variable indicating if it was created or not
    private boolean createEventFromRule(String evtDescription, DbTerm dbTerm, Integer auxDaysFromStart, Long calendarId) {

        //Calculate the new date for the event by adding auxDaysFromStart to the termStartDate
        Date newEventDate = getNewEventDate(dbTerm.getStartDate(), auxDaysFromStart);

        DbEvent event = new DbEvent();
        event.setTerm(dbTerm);
        event.setDescription(evtDescription);
        event.setDate(newEventDate);
        DbCalendar calendar = calendarService.findOne(calendarId);
        event.setCalendar(calendar);
        eventService.save(event);
        return true;
    }

    //Function that returns a new date for the event created based on a rule: add days from start to term's start date
    private Date getNewEventDate(Date auxTermStartDate, int auxDays) {
        LocalDateTime localDateTime = auxTermStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime.plusDays(auxDays);
        return DateConverter.localDateTimeToDate(localDateTime);
    }
    //**************************************************************************************************************************
    //************************************    End Of Auxiliary Functions    ****************************************************
    //**************************************************************************************************************************

}
