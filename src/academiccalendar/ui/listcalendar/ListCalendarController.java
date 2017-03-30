/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.listcalendar;

import academiccalendar.database.DBHandler;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeTableColumn;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class ListCalendarController implements Initializable {
    
    ObservableList<Calendar> list = FXCollections.observableArrayList();    
       
    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    @FXML
    private Label topLabel;
    @FXML
    private Label exit;
    @FXML
    private TableView<Calendar> tableView;
    @FXML
    private AnchorPane rootPane;    
    @FXML
    private TableColumn<Calendar, String> nameCol;
    @FXML
    private TableColumn<Calendar, String> startCol;
    @FXML
    private TableColumn<Calendar, String> endCol;
    
    // Controllers
     private FXMLDocumentController mainController ;

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
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

    @FXML
    private void load(MouseEvent event) {
        
       // TableViewSelectionModel selectionModel = tableView.getSelectionModel();
        //ObservableList selectedCells = selectionModel.getSelectedCells();
       // TablePosition tablePosition = (TablePosition) selectedCells.get(0);
        //Object val = tablePosition.getTableColumn().getCellData(0);
        //System.out.println(val.toString());
        
        // Load the calendar in the main window
        //mainController.calendarGenerate();
        
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startYear"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endYear"));
    }

  
    public static class Calendar {
        private final SimpleStringProperty name;
        private final SimpleStringProperty startYear;
        private final SimpleStringProperty endYear;
         
        public Calendar(String name, String startYear, String endYear) {
            this.name = new SimpleStringProperty(name);
            this.startYear = new SimpleStringProperty(startYear);
            this.endYear = new SimpleStringProperty(endYear);
        }
         
        public String getName() {
            return name.get();
        }

        public String getStartYear() {
            return startYear.get();
        }

        public String getEndYear() {
            return endYear.get();
        }
    }
    
    
    private void loadData() { 
        
        String qu = "SELECT * FROM CALENDARS";
        ResultSet result = databaseHandler.executeQuery(qu);
        
        try {
            while (result.next()) {
                String calendarName = result.getString("CalendarName");
                String startYear = Integer.toString(result.getInt("StartYear"));
                String endYear = Integer.toString(result.getInt("EndYear"));
                
                System.out.println(startYear);
                
                list.add(new Calendar(calendarName, startYear, endYear));
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListCalendarController.class.getName()).log(Level.SEVERE, null, ex);
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
    
}
