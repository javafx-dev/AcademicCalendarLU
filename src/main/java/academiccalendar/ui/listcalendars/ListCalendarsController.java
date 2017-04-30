/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.listcalendars;

import academiccalendar.data.model.Model;
import academiccalendar.database.DBHandler;
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

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class ListCalendarsController implements Initializable {

    // Main Controller -------------------------------
    private FXMLDocumentController mainController;
    // -------------------------------------------------------------------
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    
    // Calendar Table Fields --------------------------------------------
    ObservableList<academiccalendar.ui.main.Calendar> list = FXCollections.observableArrayList();    
    
    
    @FXML
    private Label topLabel;
    @FXML
    private TableView<academiccalendar.ui.main.Calendar> tableView;
    
    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> nameCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> springCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> fallCol;
    
    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    
    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        springCol.setCellValueFactory(new PropertyValueFactory<>("startYear"));
        fallCol.setCellValueFactory(new PropertyValueFactory<>("endYear"));
    }

     private void loadData() { 
         
        //Load all calendars into the Calendar View Table
        String qu = "SELECT * FROM CALENDARS";
        ResultSet result = databaseHandler.executeQuery(qu);
        
        try {
            while (result.next()) {
                String calendarName = result.getString("CalendarName");
                String startYear = Integer.toString(result.getInt("StartYear"));
                String endYear = Integer.toString(result.getInt("EndYear"));

                String startingDate = result.getString("StartDate");
                
                System.out.println(startYear);
                
                list.add(new academiccalendar.ui.main.Calendar(calendarName, startYear, endYear, startingDate));

               
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tableView.getItems().setAll(list);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //*** Instantiate DBHandler object *******************
        databaseHandler = new DBHandler();
        //****************************************************
        
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
        Model.getInstance().calendar_name = cal.getName();
        Model.getInstance().calendar_start = Integer.parseInt(cal.getStartYear());
        Model.getInstance().calendar_end = Integer.parseInt(cal.getEndYear());
        Model.getInstance().calendar_start_date = cal.getStartDate();
        
        // Load the calendar in the main window
        mainController.calendarGenerate();
        
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
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        //Set buttons onto the confirmation dialog
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        
        //Get the user's answer on whether deleting or not
        Optional<ButtonType> result = alert.showAndWait();
        
        //If the user wants to delete the calendar, call the function that deletes the calendar. Otherwise, close the window
        if (result.get() == buttonTypeYes){
            deleteSelectedCalendar();
        } 
        else 
        {
            // Close the window
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close(); 
        }
        
        
    }
    
    
    public void deleteSelectedCalendar() {
        
        // Get selected calendar from table
        academiccalendar.ui.main.Calendar cal = tableView.getSelectionModel().getSelectedItem();        
        String calendarName = cal.getName();
        System.out.println(calendarName);
        
        //Query that will delete all events that belong to the selected calendar
        String deleteEventsQuery = "DELETE FROM EVENTS "
                                 + "WHERE EVENTS.CalendarName='" + calendarName + "'";
        
        System.out.println(deleteEventsQuery);
        
        //Query that will delete the selected calendar, AFTER all its events had been deleted
        String deleteCalendarQuery = "DELETE FROM CALENDARS "
                                    + "WHERE CALENDARS.CalendarName='" + calendarName + "'";
        
        System.out.println(deleteCalendarQuery);
        
        //Execute query that deletes all events associated to the selected calendar
        boolean eventsWereDeleted = databaseHandler.executeAction(deleteEventsQuery);
        
        if (eventsWereDeleted)
        {
            System.out.println("All events associated to the selected calendar were successfully deleted. Deleting Calendar is next");
            //Execute query that deletes the selected calendar
            boolean calendarWasDeleted = databaseHandler.executeAction(deleteCalendarQuery);
            
            //Check if the selected calendar was deleted
            if (calendarWasDeleted)
            {
                //Show message indicating that the selected calendar was deleted
                Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
                alertMessage.setHeaderText(null);
                alertMessage.setContentText("Calendar was successfully deleted");
                alertMessage.showAndWait();
                
                // Close the window, so that when user clicks on "Manage Your Calendars" only the remaining existing calendar appear
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            }
            else
            {
                //Show message indicating that the calendar could not be deleted
                Alert alertMessage = new Alert(Alert.AlertType.ERROR);
                alertMessage.setHeaderText(null);
                alertMessage.setContentText("Deleting Calendar Failed!");
                alertMessage.showAndWait();
            }
        }
        else
        {
            //Show message indicating that the calendar could not be deleted
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Deleting Calendar Failed!");
            alertMessage.showAndWait();
            System.out.println("Deleting Events of Selected Calendar Failed!!!");
        }    
    }
    
    
    
}
