/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.addevent;

import academiccalendar.data.model.Model;
import academiccalendar.database.DBHandler;
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

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class AddEventController implements Initializable {
    
    // Controllers
     private FXMLDocumentController mainController ;
     
     
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    
    

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }

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
        
        // Get the calendar name
        String calendarName = Model.getInstance().calendar_name;
        
        // Define date format
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");        
        
        if(subject.getText().isEmpty()||termSelect.getSelectionModel().isEmpty()
                ||date.getValue() == null){
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please fill out all fields");
            alertMessage.showAndWait();
            return;
        }
        
        // Get the date value from the date picker
        String calendarDate = date.getValue().format(myFormat);
        
        // Subject for the event
        String eventSubject = subject.getText();

        // Get term that was selected by the user
        String term = termSelect.getValue();
        
        // variable that holds the ID value of the term selected by the user. It set to 0 becasue no selection has been made yet
        int chosenTermID = 0;
        
        // Get the ID of the selected term from the database based on the selected term's name
        chosenTermID = databaseHandler.getTermID(term);
        
        //---------------------------------------------------------
        //Insert new event into the EVENTS table in the database
        
        //Query to get ID for the selected Term
        String insertQuery = "INSERT INTO EVENTS VALUES ("
                + "'" + eventSubject + "', "
                + "'" + calendarDate + "', "
                + chosenTermID + ", "
                + "'" + calendarName + "'"
                + ")";
        
        
        //Check if insertion into database was successful, and show message either if it was or not
        if(databaseHandler.executeAction(insertQuery)) {
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Event was added successfully");
            alertMessage.showAndWait();
        }
        else //if there is an error
        {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Adding Event Failed!");
            alertMessage.showAndWait();
        }
        
        //Show event on the calendar
        mainController.showDate(date.getValue().getDayOfMonth(), eventSubject, chosenTermID);
            
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    
    
    private void addEventLabel(){
        
    }
    
    private void saveToDatabase(String calendarDate, String eventSubect, String program, String type, String term) {
        // Database handler function here
        // This is where you add it to the database
    }
    
    void autofillDatePicker() {
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
        
        
        //*** Instantiate DBHandler object *******************
        databaseHandler = new DBHandler();
        //****************************************************
        
        
        
        autofillDatePicker();
        
        //Get the list of exisitng terms from the database and show them in the correspondent drop-down menu
         try {
             //Get terms from database and store them in the ObservableList variable "terms"
             ObservableList<String> terms = databaseHandler.getListOfTerms();
             //Show list of terms in the drop-down menu
             termSelect.setItems(terms);
         } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
         }
   
        
        
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
