/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.addrule;

import academiccalendar.database.DBHandler;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Karis, Rodolfo, Paul, Darick
 */
public class AddRuleController implements Initializable {
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------

    DBHandler databaseHandler = new DBHandler();


    //--------------------------------------------------------------------
 
     // Controllers -------------------------------------------------------
    private FXMLDocumentController mainController ;
    
    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    // -------------------------------------------------------------------
    
    @FXML
    private Label topLabel;
    @FXML
    private JFXTextField eventDescript;
    @FXML
    private JFXComboBox<String> termSelect;
    @FXML
    private JFXTextField daysFromStart;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private AnchorPane rootPane;
    
    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<String> terms = 
        FXCollections.observableArrayList(
           "FA SEM","SP SEM", "SU SEM", 
           "FA I MBA", "FA II MBA", "SP I MBA", "SP II MBA", "SU MBA",
           "FA QTR", "WIN QTR", "SP QTR", "SU QTR",
           "FA 1st Half", "FA 2nd Half", "SP 1st Half", "SP 2nd Half",
           "Campus General", "Campus STC", "Campus BV",
           "Holiday"
        );
        
        termSelect.setItems(terms);
        
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
    private void save(MouseEvent event) {
        
         
        if(eventDescript.getText().isEmpty()||termSelect.getSelectionModel().isEmpty()
                ||daysFromStart.getText().isEmpty()){
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please fill out all fields");
            alertMessage.showAndWait();
            return;
        }
        
        // Get fields for rule
        String eventDescription = eventDescript.getText();
        int days = Integer.parseInt(daysFromStart.getText());
        String term = termSelect.getValue();
        

        //*********************************************************************
        //Save rule into the database
        saveRuleInDatabase(eventDescription, term, days);
        //*********************************************************************

        // Close the stage
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    

    
    public void saveRuleInDatabase(String eventDescription, String termName, int daysFromStart)
    {
        //Get term ID for the term selected because it is needed to save the rule in the RULES table due int attribute called TermID
        int termID = databaseHandler.getTermID(termName);
        
        //Query that will insert the rule into the RULES table in the database
        String addRuleQuery = "INSERT INTO RULES VALUES ("
                            + "'" + eventDescription + "', "
                            + termID + ", "
                            + daysFromStart
                            + ")";
        
        //print query to check it is properly written
        System.out.println(addRuleQuery);
        
        //save rule into the database and show message whether or not it was saved successfully
        if(databaseHandler.executeAction(addRuleQuery)) {
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Rule was added successfully");
            alertMessage.showAndWait();
        }
        else //if there is an error
        {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Adding Rule Failed!");
            alertMessage.showAndWait();
        }
        
    }

}
