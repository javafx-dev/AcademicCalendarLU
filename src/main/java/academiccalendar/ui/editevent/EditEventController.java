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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

        //Store the results from executing the Query
        ResultSet result = databaseHandler.executeQuery(getIDQuery);
        //Try-catch statements that will get the ID if a result was actually gotten back from the database
        try {
             while(result.next()){
                 //store ID into the corresponding variable
                 chosenTermName = result.getString("TermName");
                 System.out.println("THe termName of event to edit is: " + chosenTermName);
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
        
        //Get the list of exisitng terms from the database and show them in the correspondent drop-down menu
        ObservableList<String> termsList;
        try {
            //Get terms from database and store them in the ObservableList variable "termsList"
            termsList = databaseHandler.getListOfTerms();
            //Show list of terms in the drop-down menu
            termSelect.setItems(termsList);
        } catch (SQLException ex) {
            Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        
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
        updateEvent();
    }

    @FXML
    private void delete(MouseEvent event) {
        
        //Show confirmation dialog to make sure the user want to delete the selected event
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Event Deletion");
        alert.setContentText("Are you sure you want to delete this event?");
        //Customize the buttons in the confirmation dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        //Set buttons onto the confirmation window
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        
        //Get the user's answer on whether deleting or not
        Optional<ButtonType> result = alert.showAndWait();
        
        //If the user wants to delete the event, call the function that deletes the event. Otherwise, close the window
        if (result.get() == buttonTypeYes){
            deleteEvent();
        } 
        else 
        {
            // Close the window
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close(); 
        }
        
    }
    
    
    
    
    public void updateEvent(){
        
        // Define date format
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        //**********************************************************************
        //*******   Get OLD INFO of the Event to be edited/upated         ******
        //*******   which is the term ID, event description, event date,  ******
        //*******   and calendar name of the event to be edited           ******
        //**********************************************************************
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        String eventDate = year + "-" + month + "-" + day;
        int termID = Model.getInstance().event_term_id;
        String descript = Model.getInstance().event_subject;
        String calName = Model.getInstance().calendar_name;
        
        //Get the original date of the event to be updated in the format yyyy-mm-dd
        SimpleDateFormat auxDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String auxDate = "empty";
        Date auxEventDate = new Date();
        try {
            auxEventDate = auxDateFormat.parse(eventDate);
            auxDate = auxDateFormat.format(auxEventDate);
        } catch (ParseException ex) {
            Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(eventDate + " - " + termID + " - " + descript + " - " + calName);
        
        
        //**********************************************************************
        //******    Get NEW INFO for the Event to be edited/updated   **********
        //**********************************************************************
        
        // Get the date value from the date picker
        String newCalendarDate = date.getValue().format(myFormat);
        // Subject for the event
        String newEventSubject = subject.getText();
        // Get term that was selected by the user
        String term = termSelect.getValue();
        
        System.out.println("will be update to: ");
        System.out.println("" + newEventSubject + " - " + term + " - " + newCalendarDate + " - " + calName);
        
        //Get the ID of the new term selected by the user when editing the event's information
        int newTerm = databaseHandler.getTermID(term);
        
        //Query to will update the selected event with the new information
        String updateEventQuery = "UPDATE EVENTS"
                                + " SET "
                                + "EventDescription='" + newEventSubject + "', "
                                + "EventDate='" + newCalendarDate + "', "
                                + "TermID=" + newTerm
                                + " WHERE "
                                + "EVENTS.EventDescription='" + descript + "' AND "
                                + "EVENTS.EventDate='" + auxDate + "' AND "
                                + "EVENTS.TermID=" + termID + " AND "
                                + "EVENTS.CalendarName='" + calName + "' ";
        
        System.out.println("************************************************");
        System.out.println("query is: " + updateEventQuery);
        System.out.println("************************************************");
        
        
        //Execute query in otder to update the info for the selected event
        //and
        //Check if the update of the event in the database was successful, and show message either if it was or not
        if(databaseHandler.executeAction(updateEventQuery)) {
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Event was updated successfully");
            alertMessage.showAndWait();
            
            // Update view
            mainController.repaintView();
            
        }
        else //if there is an error
        {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Updating Event Failed!");
            alertMessage.showAndWait();
        }
        
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
        
    }
    
    
    public void deleteEvent() {
        
        //**********************************************************************
        //*******   Get INFO of the Event to be edited/upated             ******
        //*******   which is the term ID, event description, event date,  ******
        //*******   and calendar name of the event to be edited           ******
        //**********************************************************************
        int day = Model.getInstance().event_day;
        int month = Model.getInstance().event_month + 1;
        int year = Model.getInstance().event_year;
        String eventDate = year + "-" + month + "-" + day;
        int termID = Model.getInstance().event_term_id;
        String descript = Model.getInstance().event_subject;
        String calName = Model.getInstance().calendar_name;
        
        //Get the original date of the event to be updated in the format yyyy-mm-dd
        SimpleDateFormat auxDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String auxDate = "empty";
        Date auxEventDate = new Date();
        try {
            auxEventDate = auxDateFormat.parse(eventDate);
            auxDate = auxDateFormat.format(auxEventDate);
        } catch (ParseException ex) {
            Logger.getLogger(EditEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Query that will delete the selected event
        String deleteEventQuery = "DELETE FROM EVENTS "
                                + "WHERE "
                                + "EVENTS.EventDescription='" + descript + "' AND "
                                + "EVENTS.EventDate='" + auxDate + "' AND "
                                + "EVENTS.TermID=" + termID + " AND "
                                + "EVENTS.CalendarName='" + calName + "' ";
        
        System.out.println("************************************************");
        System.out.println("query is: " + deleteEventQuery);
        System.out.println("************************************************");
        
        
        //Execute query that deletes the selected event
        boolean eventWasDeleted = databaseHandler.executeAction(deleteEventQuery);
        
        if (eventWasDeleted)
        {
            //Show message indicating that the selected rule was deleted
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Selected event was successfully deleted");
            alertMessage.showAndWait();
            
            // Update view
            mainController.repaintView();
                
            // Close the window, so that when user clicks on "Manage Rules" only the remaining existing rules appear
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
        else
        {
            //Show message indicating that the rule could not be deleted
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Deleting Event Failed!");
            alertMessage.showAndWait();
        }
        
    }
    
}
