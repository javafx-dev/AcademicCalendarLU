/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.addevent;

import academiccalendar.data.model.Model;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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
    
    // Combo boxes
    @FXML
    private JFXComboBox<String> programSelect;
    @FXML
    private JFXComboBox<String> typeSelect;
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
        
        // Subject for the event
        String eventSubject = subject.getText();
        // Get program, type, and term
        String program = programSelect.getValue();
        String type = typeSelect.getValue();
        String term = termSelect.getValue();
        
        // Define date format
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Get the date value from the date picker
        String calendarDate = date.getValue().format(myFormat);
        
        // RODOLFO - ^^^ this is in the format you will need ^^^
        saveToDatabase(calendarDate, eventSubject, program, type, term);
        
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private void saveToDatabase(String calendarDate, String eventSubect, String program, String type, String term) {
        // Database handler function here
        // This is where you add it to the database
    }
    
    void autofillDatePicker() {
       // Get selected day, month, and year and autofill date selection
       int day = Model.getInstance().event_day;
       int month = Model.getInstance().event_month + 1;
       
       // Set default value for datepicker
       // Note: get Year from Model
       date.setValue(LocalDate.of(2017, month, day));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        autofillDatePicker();
        
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
        );
           
           ObservableList<String> terms = 
        FXCollections.observableArrayList(
           "FA SEM","SP SEM", "SU SEM", 
           "FA I MBA", "FA II MBA", "SP I MBA", "SP II MBA", "SU MBA",
           "FA QTR", "WIN QTR", "SP QTR", "SU QTR",
           "FA 1st Half", "FA 2nd Half", "SP 1st Half", "SP 2nd Half",
           "Campus General", "Campus STC", "Campus BV",
           "Holiday"
        );
           
          
            
        typeSelect.setItems(types);
        programSelect.setItems(programs);
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
