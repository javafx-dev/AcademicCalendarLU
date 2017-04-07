/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.main.menu;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class FileContentController implements Initializable {
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    
    // Controller --------------------------------------------------------------
    private FXMLDocumentController mainController ;

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    // -------------------------------------------------------------------------
    
    ObservableList<Calendar> list = FXCollections.observableArrayList();    
    
    // Table fields
    @FXML
    private TableView<Calendar> tableView;
    @FXML
    private TableColumn<Calendar, String> nameCol;
    @FXML
    private TableColumn<Calendar, String> startCol;
    @FXML
    private TableColumn<Calendar, String> endCol;
    
    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startYear"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endYear"));
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
            Logger.getLogger(FileContentController.class.getName()).log(Level.SEVERE, null, ex);
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
    }    

    @FXML
    private void newCalendar(MouseEvent event) {
        mainController.newCalendarEvent();
    }

    @FXML
    private void loadCalendar(MouseEvent event) {
        
        // Get selected calendar from table
        Calendar cal = tableView.getSelectionModel().getSelectedItem();
        Model.getInstance().calendar_name = cal.getName();
        Model.getInstance().calendar_start = Integer.parseInt(cal.getStartYear());
        Model.getInstance().calendar_end = Integer.parseInt(cal.getEndYear());
        
        // Load the calendar in the main window
        mainController.calendarGenerate();
    }

    @FXML
    private void pdfBtn(MouseEvent event) {
        // Export current;y open calendar
        mainController.exportCalendar();
    }
    
}
