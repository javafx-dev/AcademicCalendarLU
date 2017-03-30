/*
 * @Academic Calendar Version 1.0 3/7/2017
 * @owner and @author: FrumbSoftware
 * @Team Members: Paul Meyer, Karis Druckenmiller , Darick Cayton,Rudolfo Madriz
 */
package academiccalendar.ui.main;

import academiccalendar.data.model.Model;
import academiccalendar.database.DBHandler;
import academiccalendar.ui.addcalendar.AddCalendarController;
import academiccalendar.ui.addevent.AddEventController;
import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.sun.javaws.Main;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    @FXML
    private HBox weekdayHeader;
    
    // Hamburglar Menu
    @FXML
    private JFXHamburger hamburglar;
    @FXML
    private Pane drawerPane;
    @FXML
    private JFXDrawer drawer;
    
    // Combo/Select Boxes
    @FXML
    private JFXComboBox<String> selectedYear;
    @FXML
    private JFXListView<String> monthSelect;
    
    // Grid (has all the dates)
    @FXML
    private GridPane calendarGrid;
    
    @FXML
    private VBox centerArea;
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    
    
    // Functions
    private void editEvent(VBox day) {
        
        // ONLY for days that have labels
        if(!day.getChildren().isEmpty()) {
            
            // Get the day label
            Label lbl = (Label) day.getChildren().get(0);
            System.out.println(lbl.getText());
            
            // Store event day and month in data singleton
            Model.getInstance().event_day = Integer.parseInt(lbl.getText());
            Model.getInstance().event_month = Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());      
            Model.getInstance().event_year = Integer.parseInt(selectedYear.getValue());
            
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
    
    public void newCalendarEvent() {
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
        // Get a list of all the months (1-12) in a year
        DateFormatSymbols dateFormat = new DateFormatSymbols();
        String months[] = dateFormat.getMonths();
        
        // Add months to side selector
        monthSelect.getItems().addAll(months);   
        
        // Select the first month as default
        monthSelect.getSelectionModel().selectFirst();
        monthLabel.setText(monthSelect.getSelectionModel().getSelectedItem());
        
        // Add event listener to each month list item, allowing user to change months
        monthSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    // Show selected/current month above calendar
                    monthLabel.setText(newValue);
                    
                    // Change calendar day labels based on month selected
                    loadCalendarLabels(Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem())
                            , Model.getInstance().getMonthIndex(newValue));
                    
                    // Now, populate calendar view with the events for that month
                    populateMonthWithEvents();
                }
            });
        
              // Add event listener to each month list item, allowing user to change months
        selectedYear.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    // Change calendar day labels based on month selected
                    loadCalendarLabels(Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem())
                            , Model.getInstance().getMonthIndex(newValue));
                    
                    // Now, populate calendar view with the events for that month
                    populateMonthWithEvents();
                }
            });
    }
    
    private void loadCalendarLabels(int year, int month){
        
        // Note: Java's Gregorian Calendar class gives us the right
        // "first day of the month" for a given calendar & month
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int firstDay = gc.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // We are "offsetting" our start depending on what the
        // first day of the month is
        int offset = firstDay;
        int gridCount = 1;
        int lblCount = 1;
        
       // Go through calendar grids
       for(Node node : calendarGrid.getChildren()){
           
           VBox day = (VBox) node;
           
           day.getChildren().clear();
           day.setStyle("-fx-backgroud-color: white");
           
           // Start placing labels on the first day for the month
           if (gridCount < offset) {
               gridCount++;
               // Darken color of the offset days
               day.setStyle("-fx-background-color: #E9F2F5"); 
           } else {
            
            // Don't place a label if we've reached maximum label for the month
            if (lblCount > daysInMonth) {
                // Instead, darken day color
                day.setStyle("-fx-background-color: #E9F2F5"); 
            } else {
                
                // Make a new day label   
                Label lbl = new Label(Integer.toString(lblCount));
                lbl.setPadding(new Insets(5));
                lbl.setStyle("-fx-text-fill:darkslategray");

                day.getChildren().add(lbl);
            }
               
            lblCount++;           
           }
       }
    }
    
    public void calendarGenerate(){
        
        // Load year selection for that calendar
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_start));
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_end));

        // Select the first year by default     
        selectedYear.getSelectionModel().selectFirst();
        
        // Enable year selection box
        selectedYear.setDisable(false);
        
        // Load months
        loadMonthSelector();
        
        // Show first month automatically after Calendar is created
        loadCalendarLabels(Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem()), 0);
        
        // Show all events for that calendar (on the month user has selected)
        populateMonthWithEvents();
    }
    
    public void populateMonthWithEvents(){
        
        String calendarName = Model.getInstance().calendar_name;
        
        String currentMonth = monthLabel.getText();
        int currentMonthIndex = Model.getInstance().getMonthIndex(currentMonth) + 1;
        
        int currentYear = Integer.parseInt(selectedYear.getValue());
        
        // Query to get ALL Events from the selected calendar!!
        String getMonthEventsQuery = "SELECT * From EVENTS WHERE CalendarName='" + calendarName + "'";
        
        // Store the results here
        ResultSet result = databaseHandler.executeQuery(getMonthEventsQuery);
        
        try {
             while(result.next()){
                 
                 // Get date for the event
                 Date eventDate = result.getDate("EventDate");
                 String eventDescript = result.getString("EventDescription");
                 
                 // Check for year we have selected
                 if (currentYear == eventDate.toLocalDate().getYear()){
                     // Check for the month we already have selected (we are viewing)
                    if (currentMonthIndex == eventDate.toLocalDate().getMonthValue()){

                        // Get day for the month
                        int day = eventDate.toLocalDate().getDayOfMonth();

                        // Display decription of the event given it's day
                        showDate(day, eventDescript);
                    }         
                 }
             }
        } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void showDate(int dayNumber, String descript){
        
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("academiccalendar/ui/icons/event_icon.png"));
        ImageView imgView = new ImageView();
        imgView.setImage(img);
        
        for (Node node: calendarGrid.getChildren()) {
                
            // Get the current day    
            VBox day = (VBox) node;
            
            // Don't look at any empty days (they at least must have a day label!)
            if (!day.getChildren().isEmpty()) {
                
                // Get the day label for that day
                Label lbl = (Label) day.getChildren().get(0);
                
                // Get the number
                int currentNumber = Integer.parseInt(lbl.getText());
                
                // Did we find a match?
                if (currentNumber == dayNumber) {
                    
                    // Add an event label with the given description
                    Label eventLbl = new Label(descript); 
                    eventLbl.setGraphic(imgView);
                    eventLbl.getStyleClass().add("event-label");
                    day.getChildren().add(eventLbl);
                }
            }
        }
    }
    
    public void initializeHamburgerMenu(){
        
        try {
            VBox vox = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));
            JFXDepthManager.setDepth(drawer, 1);
            drawer.setSidePane(vox);
            
            for (Node node: vox.getChildren()){
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)-> {
                        switch(node.getAccessibleText()) {
                            case "New_Calendar" : newCalendarEvent();
                                break;
                            case "Load_Calendar" :
                                break;
                            case "Tools" :
                                break;
                            case "Views":
                                break;
                        }
                    } );
                }
            }
            
            HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(hamburglar);
            burgerTask.setRate(-1);
            hamburglar.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();   

                if(drawer.isShown()){
                     drawer.close();
                } else {
                    drawer.open();
                }

            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void initializeCalendarGrid(){
        
        // Go through each calendar grid location, or each "day" (7x6)
        int rows = 6;
        int cols = 7;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                
                // Add VBox and style it
                VBox vPane = new VBox();
                vPane.getStyleClass().add("calendar_pane");
                vPane.setPrefHeight(200);
                vPane.setPrefWidth(100);
                
                vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
                    editEvent(vPane);
                });
                
                // Add it to the grid
                calendarGrid.add(vPane, j, i);  
            }
        }       
    }
    
    
    public void initializeCalendarWeekdayHeader(){
    
        // 7 days in a week
        int weekdays = 7;
        
        // Weekday names
        String[] weekAbbr = {"Sun","Mon","Tue", "Wed", "Thu", "Fri", "Sat"};
        
        for (int i = 0; i < weekdays; i++){
            
            // Make new pane and label of weekday
            StackPane pane = new StackPane();
            pane.getStyleClass().add("weekday-header");
            
            // Make panes take up equal space
            HBox.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);
            
            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            pane.setMinWidth(weekdayHeader.getPrefWidth()/7);
            
            // Add it to the header
            weekdayHeader.getChildren().add(pane);
            
            // Add weekday name
            pane.getChildren().add(new Label(weekAbbr[i]));
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    
    // Initialize menu
    initializeHamburgerMenu();    
        
    // Make empty calendar
    initializeCalendarGrid();
    initializeCalendarWeekdayHeader();
    
    // Set Depths
    JFXDepthManager.setDepth(centerArea, 1);

    //*** Instantiate DBHandler object *******************
    databaseHandler = new DBHandler();
    //****************************************************
    
    }    
}
