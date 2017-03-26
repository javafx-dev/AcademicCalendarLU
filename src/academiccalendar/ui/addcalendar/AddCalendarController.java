/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.addcalendar;

import academiccalendar.data.model.Model;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class AddCalendarController implements Initializable {
    
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
        
        // Get the starting and ending years
        //Model.getInstance().calendar_end = Integer.parseInt(endYear.getValue());
        //Model.getInstance().calendar_start = Integer.parseInt(startYear.getValue());
        
        if ( (!startYear.getSelectionModel().isEmpty()) && (!endYear.getSelectionModel().isEmpty())
                && (!calendarName.getText().isEmpty())) {
            
            String startingYear = startYear.getSelectionModel().getSelectedItem();
            String endingYear = endYear.getSelectionModel().getSelectedItem();
            String calName = calendarName.getText();
            
            // RODOLFO - This is where you can put the Calendar name, Starting Year, and Ending Year into the Database.
            // You have the variables above (startingYear, endingYear, calName)
            // Let me know if you need more/ different fields.
            
            
            
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
        
         Model.getInstance();
        
            ObservableList<String> years = 
        FXCollections.observableArrayList(
           "2017",
           "2018",
           "2019",
           "2020",
           "2021",
           "2022",
           "2023",
           "2024"
        );
            
        startYear.setItems(years);
        endYear.setItems(years);
        
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
