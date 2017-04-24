/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.editevent;

import academiccalendar.data.model.Model;
import academiccalendar.database.DBHandler;
import academiccalendar.ui.addevent.AddEventController;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class EditEventController implements Initializable {
    
    // Main Controller -------------------------------
    private FXMLDocumentController mainController;
    // -------------------------------------------------------------------
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    @FXML
    private Label topLabel;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXDatePicker date;
    @FXML
    private AnchorPane rootPane;
    
    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    
    void autofillDatePicker() {
       // Get selected day, month, and year and autofill date selection
       int day = Model.getInstance().event_day;
       int month = Model.getInstance().event_month + 1;
       int year = Model.getInstance().event_year;
       int termID = Model.getInstance().event_term_id;
       String descript = Model.getInstance().event_subject;
       
        //Query to get ID for the selected Term
        String getIDQuery = "SELECT TermName From TERMS "
                + "WHERE TERMS.TermID= " + termID + " ";
        
        String chosenTermName = "";
        
        //System.out.println(getIDQuery);

        //Variable that stores the results from executing the Query
        // RODOLFO! - this is where I get the error. this line vvvvv
        ResultSet result = databaseHandler.executeQuery(getIDQuery);
        //Try-catch statements that will get the ID if a result was actually gotten back from the database
        try {
             while(result.next()){
                 //store ID into the corresponding variable
                 chosenTermName = result.getString("TermName");
                 System.out.println(chosenTermName);
             }
        } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Set default value for datepicker
       date.setValue(LocalDate.of(year, month, day));
       
       // Fill description field
       subject.setText(descript);
       
       termSelect.getSelectionModel().select(chosenTermName);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
        //*** Instantiate DBHandler object *******************
        databaseHandler = new DBHandler();
        //****************************************************
        
        autofillDatePicker();
        
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
    private void update(MouseEvent event) {
    }

    @FXML
    private void delete(MouseEvent event) {
    }
    
}
