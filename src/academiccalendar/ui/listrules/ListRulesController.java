/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.listrules;

import academiccalendar.ui.main.Rule;

import academiccalendar.database.DBHandler;
import academiccalendar.ui.main.FXMLDocumentController;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class ListRulesController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label topLabel;
    @FXML
    private TableView<academiccalendar.ui.main.Rule> tableView;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> eventCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> termCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> daysCol;
    
    // Main Controller -------------------------------
    private FXMLDocumentController mainController;
    // -------------------------------------------------------------------
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    
    ObservableList<academiccalendar.ui.main.Rule> list = FXCollections.observableArrayList();

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    
     public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    
    private void initCol() {
        eventCol.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));
        termCol.setCellValueFactory(new PropertyValueFactory<>("termID"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("daysFromStart"));
    }
    
    private void loadData(){
        //Load all rules into the Rule View Table
        String qu = "SELECT * FROM RULES";
        ResultSet result = databaseHandler.executeQuery(qu);
        
        try {
            while (result.next()) {
                
                String eventSubject = result.getString("EventDescription");
                
                //String termIDAux = "" + result.getInt("TermID");
                String termIDAux = Integer.toString(result.getInt("TermID"));
                //This line checks that a umber or string is at least gotten from the result that was gotten from th Results table
                System.out.println("termIDAux is: " + termIDAux);
                
                String daysFromStart = Integer.toString(result.getInt("DaysFromStart"));
                
                //these lines check each result gotten from the RULES table
                System.out.println("result is:  " + eventSubject + " - " + termIDAux + " - " + daysFromStart);
                System.out.println();
                /*
                Rule ruleObject = new Rule(eventSubject, termIDAux, daysFromStart);
                String auxID = ruleObject.getTermIDOfRule();
                System.out.println("auxID is: " + auxID);
                */
                
                list.add(new academiccalendar.ui.main.Rule(eventSubject, termIDAux, daysFromStart));
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListRulesController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void addSelectedRule(MouseEvent event) {
    }

    @FXML
    private void addAllRules(MouseEvent event) {
    }

    @FXML
    private void editRule(MouseEvent event) {
    }

    @FXML
    private void deleteRule(MouseEvent event) {
        
        // Get selected rule from table
        academiccalendar.ui.main.Rule rule = tableView.getSelectionModel().getSelectedItem();        
        String eventSubject = rule.getEventDescription();
        String auxTermID = rule.getTermID();
        System.out.println(eventSubject);
        System.out.println(auxTermID);
        
        //Query that will delete the selected rule
        String deleteRulesQuery = "DELETE FROM RULES "
                                 + "WHERE RULES.EventDescription='" + eventSubject + "' "
                                 + "AND RULES.TermID=" + auxTermID;
        
        System.out.println(deleteRulesQuery);
        
        
        //Execute query that deletes the selected rule
        boolean ruleWasDeleted = databaseHandler.executeAction(deleteRulesQuery);
        
        if (ruleWasDeleted)
        {
            //Show message indicating that the selected rule was deleted
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Selected rule was successfully deleted");
            alertMessage.showAndWait();
                
            // Close the window, so that when user clicks on "Manage Rules" only the remaining existing rules appear
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
        else
        {
            //Show message indicating that the rule could not be deleted
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Deleting Rule Failed!");
            alertMessage.showAndWait();
        }
        
    }
    
}
