/*
 * @Academic Calendar Version 1.0 3/7/2017
 * @owner and @author: FrumbSoftware
 * @Team Members: Paul Meyer, Karis Druckenmiller , Darick Cayton,Rudolfo Madriz
 */
package academiccalendar.ui.main;

import academiccalendar.data.model.Model;
import com.jfoenix.controls.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class FXMLDocumentController implements Initializable {
    
    // Root pane
    @FXML
    private AnchorPane rootPane;
    
    // Menu labels
    @FXML
    private Label newCalendarBtn;
    @FXML
    private Label saveBtn;
    @FXML
    private Label helpBtn;
    
    // Combo/Select Boxes
    @FXML
    private JFXComboBox<String> selectedYear;
    @FXML
    private JFXComboBox<String> selectedMonth;
    @FXML
    private JFXListView<String> monthSelect;
    
    // Grid (has all the dates)
    @FXML
    private GridPane calendarGrid;
    
    // Functions
    @FXML
    private void editEvent(MouseEvent event) {
        
        // Get the day that was clicked on
        Pane p = (Pane) event.getSource();
        Label lbl = (Label) p.getChildren().get(0);
        System.out.println(lbl.getText());
        
        // Store event day in data singleton
        Model.getInstance().event_day = Integer.parseInt(lbl.getText());
        
        // When user clicks on any date in the calendar, event editor window opens
        loadWindow("/academiccalendar/ui/addevent/add_event.fxml", "Event");
    }    
    
    @FXML
    private void newCalendarEvent(MouseEvent event) {
        // When the user clicks "New Calendar" pop up window that let's them enter dates
        loadWindow("/academiccalendar/ui/addcalendar/add_calendar.fxml", "Calendar");
    }
    
    private void loadWindow(String loc, String title) {
        try {
            // Create new stage from FXML given its location
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void loadMonthSelector()
    {
        // Get a list of all the months in a year
        DateFormatSymbols dateFormat = new DateFormatSymbols();
        String months[] = dateFormat.getMonths();
        
        // Add all months to the selection combo box
        selectedMonth.getItems().addAll(months);
        
        // Add each to month selector
        monthSelect.getItems().addAll(months);
        
        
        monthSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    // New month has been selected
                    selectedMonth.getSelectionModel().select(newValue);
                    
                    // Change labels based on month selected
                    loadCalendarLabels(2017, getMonthIndex(newValue));
                    
                    
                }
            });
    }
    
    private int getMonthIndex(String month){
        switch (month)
        {    
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
        }
        return 0;
    }
    
    private void loadCalendarLabels(int year, int month){
        
        // Based on month and year, get the start day week
        // and the number of days in a month
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int firstDay = gc.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("First day: " + firstDay + " Days in Month: " + daysInMonth);
        
        // Start at first day
        int labelCount = 1;
        
        // 7 days of the week
        int numWeekDays = 7;
        int dayCount = 1;
        
        int offset = numWeekDays + firstDay;
      
        // Remove old labels
        for (Node node: calendarGrid.getChildren()) {
            Pane p = (Pane) node;
            
            if (dayCount <= numWeekDays) {
               dayCount++;
            } else {
                p.getChildren().clear();
            }
        }
       
        dayCount = 1;
        
       // Go through calendar grids
       for(Node node : calendarGrid.getChildren()){
           
           Pane p = (Pane) node;
           
           // Start placing labels on the first day for the month
           if (dayCount < offset) {
               dayCount++;
           } else {
               
            // Make new day label   
            Label lbl = new Label(Integer.toString(labelCount));
            lbl.setPadding(new Insets(5));
            lbl.setStyle("-fx-text-fill:darkslategray");

            p.getChildren().add(lbl);

            // Maximum days reached for that month
            if (labelCount == daysInMonth) { System.out.print("Reached max for the month."); break;}
            labelCount++;           
           }
           
       }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
    loadMonthSelector();
    
    // ******** Everything below here is for Draggable Windows *******
    
      // Change cursor when hover over draggable area
        newCalendarBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.HAND); //Change cursor to hand
            }
        });
        
        // Change cursor when hover over draggable area
        newCalendarBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
            }
        });
        
        // Change cursor when hover over draggable area
        saveBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.HAND); //Change cursor to hand
            }
        });
        
        // Change cursor when hover over draggable area
        saveBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
            }
        });
        
        // Change cursor when hover over draggable area
        helpBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.HAND); //Change cursor to hand
            }
        });
        
        // Change cursor when hover over draggable area
        helpBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
            }
        });
    
    }    

}
