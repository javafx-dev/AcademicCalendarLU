/*
 * @Academic Calendar Version 1.0 3/7/2017
 * @owner and @author: FrumbSoftware
 * @Team Members: Paul Meyer, Karis Druckenmiller , Darick Cayton,Rudolfo Madriz
 */
package academiccalendar;

import com.jfoenix.controls.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class FXMLDocumentController implements Initializable {
    
    // All Calendar Fields here
    
    // ***** I commented these out because I need to update them with the
    // GUI changes I've made. *****
    
    /*// All Mondays
    @FXML
    private Label monLabel;
    @FXML
    private Label monLabel1;
    @FXML
    private Label monLabel2;
    @FXML
    private Label monLabel3;
    @FXML
    private Label monLabel4;
    @FXML
    private Label monLabel5;
    @FXML
    private Rectangle monday1;
    @FXML
    private Rectangle monday2;
    @FXML
    private Rectangle monday3;
    @FXML
    private Rectangle monday4;
    // monday5 should only be shown if month actually has 5 Mondays
    @FXML
    private Rectangle monday5;
    
    
    
    // All Tuedays
    @FXML
    private Label tueLabel;
    @FXML
    private Label tueLabel1;
    @FXML
    private Label tueLabel2;
    @FXML
    private Label tueLabel3;
    @FXML
    private Label tueLabel4;
    @FXML
    private Label tueLabel5;
    @FXML
    private Rectangle tuesday1;
    @FXML
    private Rectangle tuesday2;
    @FXML
    private Rectangle tuesday3;
    @FXML
    private Rectangle tuesday4;
    // tuesday5 should only be shown if month has 5 tuedsdays
    @FXML
    private Rectangle tuesday5;
    
    // All Wednesdays
    @FXML
    private Label wedLabel;
    @FXML
    private Label wedLabel1;
    @FXML
    private Label wedLabel2;
    @FXML
    private Label wedLabel3;
    @FXML
    private Label wedLabel4;
    @FXML
    private Label wedLabel5;
    @FXML
    private Rectangle wednesday1; 
    @FXML
    private Rectangle wednesday2;
    @FXML
    private Rectangle wednesday3;
    @FXML
    private Rectangle wednesday4;
    @FXML
    private Rectangle wednesday5;
    
    //All Thursdays
    @FXML
    private Label thLabel;
    @FXML
    private Label thLabel1;
    @FXML
    private Label thLabel2;
    @FXML
    private Label thLabel3;
    @FXML
    private Label thLabel4;
    @FXML
    private Label thLabel5;
    @FXML
    private Rectangle thursday1;
    @FXML
    private Rectangle thursday3;
    @FXML
    private Rectangle thursday2;
    @FXML
    private Rectangle thursday4;
    @FXML
    private Rectangle thursday5;
    
    //All Fridays
    @FXML
    private Label frLabel;
    @FXML
    private Label frLabel1;
    @FXML
    private Label frLabel2;
    @FXML
    private Label frLabel3;
    @FXML
    private Label frLabel4;
    @FXML
    private Label frLabel5;
    @FXML
    private Rectangle friday1;    
    @FXML
    private Rectangle friday2;
    @FXML
    private Rectangle friday3;
    @FXML
    private Rectangle friday4;    
    @FXML
    private Rectangle friday5;  
    
    //All Saturdays
     @FXML
    private Label satLabel;
    @FXML
    private Label satLabel1;
    @FXML
    private Label satLabel2;
    @FXML
    private Label satLabel3;
    @FXML
    private Label satLabel4;
    @FXML
    private Label satLabel5;
   
    @FXML
    private Rectangle saturday1;
    @FXML
    private Rectangle saturday2;
    @FXML
    private Rectangle saturday3;
    @FXML
    private Rectangle saturday4;
    @FXML
    private Rectangle saturday5;
    
    //All Sundays
    @FXML
    private Label sunLabel;
    @FXML
    private Label sunLabel1;
    @FXML
    private Label sunLabel2;
    @FXML
    private Label sunLabel3;
    @FXML
    private Label sunLabel4;
    @FXML
    private Label sunLabel5;
    
    @FXML
    private Rectangle sunday1;

    @FXML
    private Rectangle sunday2;

    @FXML
    private Rectangle sunday3;

    @FXML
    private Rectangle sunday4;

    @FXML
    private Rectangle sunday5;    */

    // Tabs
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab tab_term;
    @FXML
    private Tab tab_rules;
    
    //Menu
    @FXML
    private MenuBar mnuMain;
    @FXML
    private MenuItem newF;
    @FXML
    private MenuItem saveF;
    @FXML
    private MenuItem openF;
    
    //  Term Tab Controls
    @FXML
    private Pane term_pane;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXDatePicker endDate;
    @FXML
    private JFXColorPicker eventColor;
    @FXML
    private JFXCheckBox endDateCheck; 
    @FXML
    private JFXTextField eDescription;
    
    // Combo/Select Boxes
    @FXML
    private JFXComboBox<String> termSelect = new JFXComboBox<String>();
    @FXML
    private JFXComboBox<String> yearSelect = new JFXComboBox <String>();
    
    // Main Tab Labels
    @FXML
    private Label eEndLabel;
    @FXML
    private Label eStartLabel;
    
    // Grid
    @FXML
    private GridPane calendarGrid;
    
     @FXML
    void editEvent(MouseEvent event) {
        EventEditor.display();
    }

    // Get user input from Date Picker
    private void getCalenderDates()
    {
        // Can make these fields global
        int startYear = startDate.getValue().getYear();
        int endYear = endDate.getValue().getYear();
        
        int startMonth = startDate.getValue().getMonthValue();
        int endMonth = endDate.getValue().getMonthValue();
        
        int startDay =  startDate.getValue().getDayOfMonth();
        int endDay =  endDate.getValue().getDayOfMonth();        
    }
    
    private void getEventDescr()
    {
        String eDescrip;
        eDescrip = eDescription.getText();
    }
    
    private boolean isEndDateChecked(boolean checked)
    {
        checked = endDateCheck.isSelected();
        return checked;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
             
          ObservableList<String> terms = 
    FXCollections.observableArrayList(
       "MBA SEM",
       "Undergraduate SEM",
       "Quarter SEM",
       "Half SEM"      
    );
      
    termSelect.setItems(terms);
    
      ObservableList<String> years = 
    FXCollections.observableArrayList(
       "2017",
       "2018",
       "2019",
       "2020",
       "2021",
       "2022",
       "2023",
       "2024",
       "2025"
            
    );
      
    yearSelect.setItems(years);
    
    }    

}
