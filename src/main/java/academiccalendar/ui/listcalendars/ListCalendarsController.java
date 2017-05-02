package academiccalendar.ui.listcalendars;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbCalendar;
import academiccalendar.service.CalendarService;
import academiccalendar.ui.main.FXMLDocumentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ListCalendarsController implements Initializable {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ListCalendarsController.class);

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private CalendarService calendarService;

    @FXML
    private Label topLabel;
    @FXML
    private TableView<academiccalendar.ui.main.Calendar> tableView;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> nameCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> springCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> fallCol;

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;

    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        springCol.setCellValueFactory(new PropertyValueFactory<>("startYear"));
        fallCol.setCellValueFactory(new PropertyValueFactory<>("endYear"));
    }

    private void loadData() {
        LOGGER.info("Loading calendars start");
        Iterable<DbCalendar> calendars = calendarService.findAll();
        ObservableList<academiccalendar.ui.main.Calendar> list = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (DbCalendar calendar : calendars) {
            String date = sdf.format(calendar.getStartDate());
            list.add(new academiccalendar.ui.main.Calendar(calendar.getName(), Integer.toString(calendar.getStartYear()), Integer.toString(calendar.getEndYear()), date, calendar));
        }
        tableView.getItems().setAll(list);
        LOGGER.info("Loading calendars end");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initCol();
        loadData();

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
    private void openCalendar(MouseEvent event) {
        // Get selected calendar from table
        academiccalendar.ui.main.Calendar cal = tableView.getSelectionModel().getSelectedItem();
        Model.getInstance().calendar_id = cal.getDbCalendar().getId();

        // Load the calendar in the main window
        mainController.calendarGenerate(cal.getDbCalendar());

        // Close the window after opening and loading the selected calendar
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void deleteCalendar(MouseEvent event) {

        //Show confirmation dialog to make sure the user want to delete the selected rule
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Calendar Deletion");
        alert.setContentText("Are you sure you want to delete this calendar?");
        //Customize the buttons in the confirmation dialog
        ButtonType buttonTypeYes = ButtonType.YES;
        ButtonType buttonTypeNo = ButtonType.NO;
        //Set buttons onto the confirmation dialog
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        //Get the user's answer on whether deleting or not
        Optional<ButtonType> result = alert.showAndWait();

        //If the user wants to delete the calendar, call the function that deletes the calendar. Otherwise, close the window
        if (result.get() == ButtonType.YES) {
            deleteSelectedCalendar();
        } else {
            // Close the window
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }


    }

    private void deleteSelectedCalendar() {
        // Get selected calendar from table
        academiccalendar.ui.main.Calendar cal = tableView.getSelectionModel().getSelectedItem();
        DbCalendar calendarName = cal.getDbCalendar();
        LOGGER.info("Deleting calendar, name: {}", calendarName);
        calendarService.delete(calendarName);
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Calendar was successfully deleted");
        alertMessage.showAndWait();
        // Close the window, so that when user clicks on "Manage Your Calendars" only the remaining existing calendar appear
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


}
