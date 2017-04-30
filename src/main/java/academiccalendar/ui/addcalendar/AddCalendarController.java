/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.addcalendar;

import academiccalendar.data.model.Model;
import academiccalendar.database.DBHandler;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Karis
 */
@Component
public class AddCalendarController implements Initializable {
    
    @Autowired
    private DBHandler databaseHandler;

    // Controllers
    @Autowired
    private FXMLDocumentController mainController;

    @FXML
    private Label topLabel;
    @FXML
    private Label exit;
    @FXML
    private JFXTextField calendarName;
    @FXML
    private JFXComboBox<String> startYear;
    @FXML
    private JFXComboBox<String> endYear;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXButton generate;
    @FXML
    private JFXButton cancel;
    
    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    void generateNewCalendar(MouseEvent event) {
        
        if ( (date.getValue() != null) && (!calendarName.getText().isEmpty())) {            
            
            // Set the starting and ending years, and the starting date
            Model.getInstance().calendar_start_date = "" + date.getValue();
            System.out.println("*-**--*--*-*-*-*--*-*-*-*-*-*-*-*--*");
            System.out.println("*-**--*--*-*-*-*--*-*-*-*-*-*-*-*--*");
            System.out.println("Calendar starting DATE is:  " + Model.getInstance().calendar_start_date);
            System.out.println("*-**--*--*-*-*-*--*-*-*-*-*-*-*-*--*");
            System.out.println("*-**--*--*-*-*-*--*-*-*-*-*-*-*-*--*");
            Model.getInstance().calendar_start = date.getValue().getYear();
            Model.getInstance().calendar_end = date.getValue().getYear() + 1;
            Model.getInstance().calendar_name = calendarName.getText();
            
            
            
            // Define date format
            DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Get the date value from the date picker
            String startingDate = date.getValue().format(myFormat);
            System.out.println("Calendar starting DATE format is now:  " + startingDate);
            System.out.println("*-**--*--*-*-*-*--*-*-*-*-*-*-*-*--*");
            System.out.println("*-**--*--*-*-*-*--*-*-*-*-*-*-*-*--*");
            
            //String startingDate = Model.getInstance().calendar_start_date;
            String startingYear = Integer.toString(Model.getInstance().calendar_start);
            String endingYear = Integer.toString(Model.getInstance().calendar_end);
            String calName = calendarName.getText();
            
            // Query that inserts the new calendar into the database
            String calendarQuery = "INSERT INTO CALENDARS VALUES ("
                    + "'" + calName + "', " + startingYear + ", " + endingYear + ", " + "'" + startingDate + "')";
            
            System.out.println(calendarQuery); //to test the query that will be sent to the database is written correctly 
            
            //Insert the new calendar into the database and show a message wheher the insertion was successful or not
            if(databaseHandler.executeAction(calendarQuery)) 
            {
                Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
                alertMessage.setHeaderText(null);
                alertMessage.setContentText("Calendar was created successfully");
                alertMessage.showAndWait();
                
                // Load the calendar in the main window
                mainController.calendarGenerate();
            }
            else //if there is an error
            {
                Alert alertMessage = new Alert(Alert.AlertType.ERROR);
                alertMessage.setHeaderText(null);
                alertMessage.setContentText("Creating Calendar Failed!");
                alertMessage.showAndWait();
            }
            
            // Close the window
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
        else {
            System.out.println("Error: user did not fill out all fields");
            
            Alert alert = new Alert(AlertType.WARNING, "Please fill out all fields.");
            alert.showAndWait();
        }        
    }
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // ******** Code below is for Draggable windows **********    
        
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
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
}
