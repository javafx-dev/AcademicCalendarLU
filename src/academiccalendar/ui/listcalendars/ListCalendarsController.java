/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.listcalendars;

import academiccalendar.data.model.Model;
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
    }

    @FXML
    private void deleteCalendar(MouseEvent event) {
    }
    
}
