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
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
        
        
        System.out.println("---------------------");
        System.out.println(term);
        System.out.println("Getting ID for selected Term");
        
        //Query to get ID for the selected Term
        String getIDQuery = "SELECT TermID From TERMS "
                + "WHERE TERMS.TermName='" + term + "' ";
        
        System.out.println(getIDQuery);

        //Variable that stores the results from executing the Query
        ResultSet result = databaseHandler.executeQuery(getIDQuery);
        //Try-catch statements that will get the ID if a result was actually gotten back from the database
        try {
             while(result.next()){
                 //store ID into the corresponding variable
                 chosenTermID = Integer.parseInt(result.getString("TermID"));
                 System.out.println(chosenTermID);
             }
        } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
        System.out.println("---------------------");
        System.out.println(program);
        System.out.println("Getting ID for selected PROGRAM");
        
        
        //Query to get ID for the selected Program
        getIDQuery = "SELECT ProgramID From PROGRAMS "
                + "WHERE PROGRAMS.ProgramName='" + program + "' ";
        
        System.out.println(getIDQuery);

        //Variable that stores the results from executing the Query
        result = databaseHandler.executeQuery(getIDQuery);
        //Try-catch statements that will get the ID if a result was actually gotten back from the database
        try {
             while(result.next()){
                 //store ID into the corresponding variable
                 chosenProgramID = Integer.parseInt(result.getString("ProgramID"));
                 System.out.println(chosenProgramID);
             }
        } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.out.println("---------------------");
        System.out.println(type);
        System.out.println("Getting ID for selected EVENT TYPE");
        
        //Query to get ID for the selected Term
        getIDQuery = "SELECT EventTypeID From EVENT_TYPES "
                + "WHERE EVENT_TYPES.EventTypeName='" + type + "' ";
        
        System.out.println(getIDQuery);

        //Variable that stores the results from executing the Query
        result = databaseHandler.executeQuery(getIDQuery);
        //Try-catch statements that will get the ID if a result was actually gotten back from the database
        try {
             while(result.next()){
                 //store ID into the corresponding variable
                 chosenEventTypeID = Integer.parseInt(result.getString("EventTypeID"));
                 System.out.println(chosenEventTypeID);
             }
        } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        
        // Get calendar name from calendar table
        //---------------------------------------------------------
        //Insert new event into the EVENTS table in the database
        
        //Query to get ID for the selected Term
        String insertQuery = "INSERT INTO EVENTS VALUES ("
                + "'" + eventSubject + "', "
                + "'" + calendarDate + "', "
                + chosenTermID + ", "
                + "'" + calendarName + "'"  // This value will have to be replaced later by the name that the user choose to put on the calendar he or she generates at the beginning
                + ")";
        
        /*
        String insertQuery = "INSERT INTO EVENTS VALUES ("
                + chosenEventTypeID + ", "
                + chosenTermID + ", "
                + chosenProgramID + ", "
                + "'" + eventSubject + "', "
                + "'" + calendarDate + "', "
                + "'" + calendarName + "'"  // This value will have to be replaced later by the name that the user choose to put on the calendar he or she generates at the beginning
                + ")";
        */
        
        System.out.println(insertQuery);
        
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
        
        mainController.showDate(date.getValue().getDayOfMonth(), eventSubject, chosenTermID);

        // RODOLFO - ^^^ this is in the format you will need ^^^
        //saveToDatabase(calendarDate, eventSubject, program, type, term);
        
        
        //Reloads all available calendars in the database of the table view for loading calendars
        //loadData();
        
        
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
        
        // Note: Client says she only wants terms, and the term determines the program.
        /*
            ObservableList<String> types = 
        FXCollections.observableArrayList(
           "Academic",
           "Holiday",
           "Sports",
           "Campus"      
        ); 
            
           ObservableList<String> programs = 
        FXCollections.observableArrayList(
           "Undergraduate",
           "Graduate (MBA)",
           "Online",
           "Accelerate Program"      
        ); */
           
           ObservableList<String> terms = 
        FXCollections.observableArrayList(
           "FA SEM","SP SEM", "SU SEM", 
           "FA I MBA", "FA II MBA", "SP I MBA", "SP II MBA", "SU MBA",
           "FA QTR", "WIN QTR", "SP QTR", "SU QTR",
           "FA 1st Half", "FA 2nd Half", "SP 1st Half", "SP 2nd Half",
           "Campus General", "Campus STC", "Campus BV",
           "Holiday"
        );
           
          
            
        //typeSelect.setItems(types);
        //programSelect.setItems(programs);
        termSelect.setItems(terms);
        
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
