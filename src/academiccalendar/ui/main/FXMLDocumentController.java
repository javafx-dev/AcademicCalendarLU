/*
 * @Academic Calendar Version 1.0 3/7/2017
 * @owner and @author: FrumbSoftware
 * @Team Members: Paul Meyer, Karis Druckenmiller , Darick Cayton,Rudolfo Madriz
 */
package academiccalendar.ui.main;

import academiccalendar.data.model.Model;
import academiccalendar.ui.addcalendar.AddCalendarController;
import academiccalendar.ui.addevent.AddEventController;
import com.jfoenix.controls.*;
import com.sun.javaws.Main;
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
import javafx.event.ActionEvent;
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
import javafx.stage.Modality;
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
    @FXML
    private Label monthLabel;
    @FXML
    private Label calendarNameLabel;
    
    // Combo/Select Boxes
    @FXML
    private JFXComboBox<String> selectedYear;
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
        
        // ONLY for days that have labels
        if(!p.getChildren().isEmpty()) {
            
            // Get the day label
            Label lbl = (Label) p.getChildren().get(0);
            System.out.println(lbl.getText());
            
            // Store event day and month in data singleton
            Model.getInstance().event_day = Integer.parseInt(lbl.getText());
            Model.getInstance().event_month = Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());          
            
            // When user clicks on any date in the calendar, event editor window opens
            try {
               // Load root layout from fxml file.
               FXMLLoader loader = new FXMLLoader();
               loader.setLocation(getClass().getResource("/academiccalendar/ui/addevent/add_event.fxml"));
               AnchorPane rootLayout = (AnchorPane) loader.load();
               Stage stage = new Stage(StageStyle.UNDECORATED);
               stage.initModality(Modality.APPLICATION_MODAL); 

               AddEventController eventController = loader.getController();
               eventController.setMainController(this);
               // Show the scene containing the root layout.
               Scene scene = new Scene(rootLayout);
               stage.setScene(scene);
               stage.show();
           } catch (IOException ex) {
               Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
           }
            
        }
    }    
    
    @FXML
    private void newCalendarEvent(ActionEvent event) {
        // When the user clicks "New Calendar" pop up window that let's them enter dates
         try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/academiccalendar/ui/addcalendar/add_calendar.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); 

            AddCalendarController calendarController = loader.getController();
            calendarController.setMainController(this);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
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
        
        // Add each to month selector
        monthSelect.getItems().addAll(months);        
        
        monthSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    // Show selected month above calendar
                    monthLabel.setText(newValue + " " + selectedYear.getSelectionModel().getSelectedItem());
                    
                    // Change labels based on month selected
                    loadCalendarLabels(Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem())
                            , Model.getInstance().getMonthIndex(newValue));
                }
            });
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
                p.setStyle("-fx-backgroud-color: white");
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
       
       // Darken calendar pane's with no labels
        for (Node node: calendarGrid.getChildren()) {
            Pane p = (Pane) node;
            
            if (dayCount <= numWeekDays) {
               dayCount++;
            } else {
                if (p.getChildren().isEmpty()) {
                    p.setStyle("-fx-background-color: #E9F2F5");
                }
                
            }
        }
    }
    
    public void calendarGenerate(){
        // Load the combo box with years for that calendar (always ending and starting year)
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_start));
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_end));
        
        selectedYear.getSelectionModel().selectFirst();
        
        // Enable year selection box
        selectedYear.setDisable(false);
        
        // Set calendar name to calendar label
        //calendarNameLabel.setText(Model.getInstance().calendar_name);
        
        // Load months
        loadMonthSelector();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    
    /*// ******** Everything below here is for Styling Windows *******
    
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
        });*/
    
    }    

}
