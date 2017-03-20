/*
 * @Academic Calendar Version 1.0 3/7/2017
 * @owner and @author: FrumbSoftware
 * @Team Members: Paul Meyer, Karis Druckenmiller , Darick Cayton,Rudolfo Madriz
 */
package academiccalendar.ui.main;

import com.jfoenix.controls.*;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class FXMLDocumentController implements Initializable {
    
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
    @FXML
    private JFXListView<Pane> monthSelect;
    
    // Main Tab Labels
    @FXML
    private Label eEndLabel;
    @FXML
    private Label eStartLabel;
    
    // Grid
    @FXML
    private GridPane calendarGrid;
    
    // Functions
    @FXML
    void editEvent(MouseEvent event) {
        // When user clicks on any date in the calendar, event editor window opens
        loadWindow("/academiccalendar/ui/addevent/add_event.fxml", "Event");
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
        
        String test = startDate.getValue().format(DateTimeFormatter.ISO_DATE);
        System.out.println(test);
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
    
    private void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void loadMonthSelector(ObservableList<String> months)
    {
        double labelHeight = monthSelect.getPrefHeight() / months.size();
        
        for (int i = 0; i < months.size(); i++) {
            
            Label lbl = new Label(months.get(i));
            Pane pane = new Pane();
            pane.getChildren().add(lbl);
            
            pane.getStyleClass().add("month-select-pane");            
            
            pane.setMaxWidth(monthSelect.getWidth());
            pane.setPrefSize(monthSelect.getWidth(), labelHeight);
            pane.minHeight(labelHeight);
            monthSelect.getItems().add(pane);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<String> months = 
    FXCollections.observableArrayList(
       "January",
       "February",
       "March",
       "April",
       "May",
       "June",
       "July",
       "August",
       "September",
       "October",
       "November",
       "December"
    );
    
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
    
    loadMonthSelector(months);
    
    }    

}
