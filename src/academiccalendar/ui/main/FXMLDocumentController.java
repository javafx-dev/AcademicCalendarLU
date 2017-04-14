

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
import academiccalendar.ui.addrule.AddRuleController;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FXMLDocumentController implements Initializable {
    
    // Root pane
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private Label monthLabel;
    @FXML
    private HBox weekdayHeader;
    
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
    
    @FXML
    private ScrollPane scrollPane;
    
    // Calendar Table Fields --------------------------------------------
    ObservableList<academiccalendar.ui.main.Calendar> list = FXCollections.observableArrayList();    
    

    ObservableList<academiccalendar.ui.main.Rule> listOfRules = FXCollections.observableArrayList();

    private TableView<academiccalendar.ui.main.Calendar> tableView;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> nameCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> startCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Calendar, String> endCol;
    @FXML
    private TableView<academiccalendar.ui.main.Calendar> calendarTableView;
    @FXML

    private TableView<academiccalendar.ui.main.Rule> ruleTableView;
    
    // KARIS ---> are the following lines correct? I don't think this is right to do
    /*
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> descriptionCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> termCol;
    @FXML
    private TableColumn<academiccalendar.ui.main.Rule, String> daysCol;
    */
    

    // ------------------------------------------------------------------
    
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
    
    public void newRuleEvent() {
        // When the user clicks "New Rule" pop up window appears
         try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/academiccalendar/ui/addrule/add_rule.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); 

            AddRuleController ruleController = loader.getController();
            ruleController.setMainController(this);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void newRuleEvent() {
        // When the user clicks "New Rule" pop up window appears
         try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/academiccalendar/ui/addrule/add_rule.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); 

            AddRuleController ruleController = loader.getController();
            ruleController.setMainController(this);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initializeMonthSelector(){
        // Add event listener to each month list item, allowing user to change months
        monthSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    // Note: The line of code "monthSelect.getItems().clear()" invokes this change listener
                    // and the newValue becomes NULL. When that happens, we will just skip the following instructions
                    // as if nothing happened.
                    // See method calendarGenerate() for that call.
                    if (newValue != null) {
                        // Show selected/current month above calendar
                        monthLabel.setText(newValue);

                        // Change calendar day labels based on month selected
                        loadCalendarLabels(Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem())
                                , Model.getInstance().getMonthIndex(newValue));

                        // Now, populate calendar view with the events for that month
                        populateMonthWithEvents();
                    }
                    
                }
            });
        
              // Add event listener to each month list item, allowing user to change months
        selectedYear.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    if (newValue != null){
                        // Change calendar day labels based on month selected
                        loadCalendarLabels(Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem())
                                , Model.getInstance().getMonthIndex(newValue));

                        // Now, populate calendar view with the events for that month
                        populateMonthWithEvents();
                    }
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
        
        // Load year selection for calendar
        selectedYear.getItems().clear(); // Invokes our change listener
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_start));
        selectedYear.getItems().add(Integer.toString(Model.getInstance().calendar_end));

        // Select the first YEAR as default     
        selectedYear.getSelectionModel().selectFirst();
        
        // Enable year selection box
        selectedYear.setVisible(true);
        
        // Get a list of all the months (1-12) in a year
        DateFormatSymbols dateFormat = new DateFormatSymbols();
        String months[] = dateFormat.getMonths();
        
        // Load month section for calendar
        monthSelect.getItems().clear();  // Invokes our change listener
        monthSelect.getItems().addAll(months);   
        
        // Select the first MONTH as default
        monthSelect.getSelectionModel().selectFirst();
        monthLabel.setText(monthSelect.getSelectionModel().getSelectedItem());
        
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
    
    public void exportCalendarPDF()
    {
         TableView<Event> table = new TableView<Event>();
         ObservableList<Event> data =FXCollections.observableArrayList();  
   
        
        double w = 500.00;
        // set width of table view
        table.setPrefWidth(w);
        // set resize policy
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // intialize columns
        TableColumn<Event,String> term  = new TableColumn<Event,String>("Term");
        TableColumn<Event,String> subject  = new TableColumn<Event,String>("Subject");
        TableColumn<Event,String> date = new TableColumn<Event,String>("Date");
        // set width of columns
        term.setMaxWidth( 1f * Integer.MAX_VALUE * 20 ); // 50% width
        subject.setMaxWidth( 1f * Integer.MAX_VALUE * 60 ); // 50% width
        date.setMaxWidth( 1f * Integer.MAX_VALUE * 20 ); // 50% width
        // 
        term.setCellValueFactory(new PropertyValueFactory<Event,String>("term"));
        subject.setCellValueFactory( new PropertyValueFactory<Event,String>("subject"));
        date.setCellValueFactory(new PropertyValueFactory<Event,String>("date"));
        
        // Add columns to the table
        table.getColumns().add(term);
        table.getColumns().add(subject);
        table.getColumns().add(date);
        
        String calendarName = Model.getInstance().calendar_name;
        
        // Query to get ALL Events from the selected calendar!!
        String getMonthEventsQuery = "SELECT * From EVENTS WHERE CalendarName='" + calendarName + "'";   
        
        // Store the results here
        ResultSet result = databaseHandler.executeQuery(getMonthEventsQuery);
        
         try {
             
             while(result.next()){
                //initalize temporarily strings
                 String tempTerm="";
                
                 
                //***** Get term, Event Description and Date *****
                
                //Get Event Description
                 String eventDescript = result.getString("EventDescription");
                //Get Term ID for an event
                 int termID = result.getInt("TermID");
                 
                //Get Event Date and format it as day-month-year
                 Date dDate=result.getDate("EventDate");
                 DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                 String eventDate = df.format(dDate);
                 
                 //Query that will get the term name based on a term ID
                 String getTermQuery = "SELECT TermName FROM TERMS WHERE TermID=" + termID + ""; 
                 //Execute query to get TermName and store it in a ResultSet variable
                 ResultSet termResult = databaseHandler.executeQuery(getTermQuery);
                 
                 
                 while(termResult.next())
                 {
                      tempTerm=termResult.getString(1);
                     /*
                      while(programResult.next())
                        {
                           tempProgram = programResult.getString(1);
                        }
                      tempTerm+=" "+tempProgram;
                    */
                 }
                 
                
                //Get Term Name for an event
                //tempTerm = termResult.getString(1);
                
                
                //Add event information in a row
                data.add(new Event(tempTerm,eventDescript,eventDate));
             
             }
        } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        } 
         
         
       
        table.getItems().setAll(data);
        // open dialog window and export table as pdf
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null){
          job.printPage(table);
          job.endJob();
        }
       }
    
    
     public void exportCalendarExcel() 
    {
         
         

      
        
        
        String calendarName = Model.getInstance().calendar_name;
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet =wb.createSheet(calendarName);
        
        XSSFRow row = sheet.createRow(1);
        XSSFCell cell;
        
        cell = row.createCell(1);
        cell.setCellValue("Term");
        cell = row.createCell(2);
        cell.setCellValue("Subject");
        cell = row.createCell(3);
        cell.setCellValue("Date");
        
        

       
        // Query to get ALL Events from the selected calendar!!
        String getMonthEventsQuery = "SELECT * From EVENTS WHERE CalendarName='" + calendarName + "'";   
        
        // Store the results here
        ResultSet result = databaseHandler.executeQuery(getMonthEventsQuery);
        
         try {
             int counter=2;
             while(result.next()){
                
                 //initalize temporarily strings
                 String tempTerm="";
                
                 
                //***** Get term, Event Description and Date *****
                
                //Get Event Description
                 String eventDescript = result.getString("EventDescription");
                //Get Term ID for an event
                 int termID = result.getInt("TermID");
                 
                //Get Event Date and format it as day-month-year
                 Date dDate=result.getDate("EventDate");
                 DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                 String eventDate = df.format(dDate);
                 
                 //Query that will get the term name based on a term ID
                 String getTermQuery = "SELECT TermName FROM TERMS WHERE TermID=" + termID + "";
                 //Execute query to get TermName and store it in a ResultSet variable
                 ResultSet termResult = databaseHandler.executeQuery(getTermQuery);
                 
                /* 
                //initalize temporarily strings
                 String tempTerm="";
                 String tempProgram="";
                 // Get term, program, Event Description and Date
                 
                 String eventDescript = result.getString("EventDescription");
                 int termID = result.getInt("TermID");
                 
                 
                 Date dDate=result.getDate("EventDate");
                 DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                 String eventDate = df.format(dDate);
                 
                 int programID = result.getInt("ProgramID");
                 String getProgramQuery = "SELECT ProgramName FROM PROGRAMS WHERE ProgramID=" + programID + "";
                 String getTermQuery = "SELECT TermName FROM TERMS WHERE TermID=" + termID + ""; 
                 ResultSet termResult = databaseHandler.executeQuery(getTermQuery);
                 ResultSet programResult = databaseHandler.executeQuery(getProgramQuery);
                 */
                
                 while(termResult.next())
                 {
                      tempTerm=termResult.getString(1);
                     
                      /*
                      while(programResult.next())
                        {
                           tempProgram = programResult.getString(1);
                        }
                      tempTerm+=" "+tempProgram;
                      */
                 }
                
                //Get Term Name for an event
                //tempTerm = termResult.getString("TermName");
                
                
                row = sheet.createRow(counter);
                cell = row.createCell(1);
                cell.setCellValue(tempTerm);
                cell = row.createCell(2);
                cell.setCellValue(eventDescript);
                cell = row.createCell(3);
                cell.setCellValue(eventDate);
              for (int i=0; i<3; i++){
               sheet.autoSizeColumn(i);
            }
               
                counter++;
             
             }
        } catch (SQLException ex) {
             Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        } 
         try{
        FileOutputStream out = new FileOutputStream(new File(calendarName + ".xlsx"));
        wb.write(out);
        out.close();
       
         }
         catch(Exception e) {
            e.printStackTrace();
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
                vPane.setMinWidth(weekdayHeader.getPrefWidth()/7);
                
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
        
    // Make empty calendar
    initializeCalendarGrid();
    initializeCalendarWeekdayHeader();
    initializeMonthSelector();
    
    
    // Set Depths
    JFXDepthManager.setDepth(scrollPane, 1);

    //*** Instantiate DBHandler object *******************
    databaseHandler = new DBHandler();
    //****************************************************
    
    // Initialize tables
    initCol();
    loadData();
    
    }    
    
    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startYear"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endYear"));

        
        //descriptionCol.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));
        //termCol.setCellValueFactory(new PropertyValueFactory<>("termName"));
        //daysCol.setCellValueFactory(new PropertyValueFactory<>("daysFromStart"));

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
        
        calendarTableView.getItems().setAll(list);

        
        //********************************************************************************************************
        
        //Load all rules into the Rule View Table
        qu = "SELECT * FROM RULES";
        result = databaseHandler.executeQuery(qu);
        
        try {
            while (result.next()) {
                String eventSubject = result.getString("EventDescription");
                String termID = Integer.toString(result.getInt("TermID"));
                String daysFromStart = Integer.toString(result.getInt("DaysFromStart"));
                
                System.out.println();
                
                listOfRules.add(new academiccalendar.ui.main.Rule(eventSubject, termID, daysFromStart));
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ruleTableView.getItems().setAll(listOfRules);

    }
    
    @FXML
    private void newCalendar(MouseEvent event) {
        newCalendarEvent();

    }

    @FXML
    private void loadCalendar(MouseEvent event) {
        // Get selected calendar from table
        academiccalendar.ui.main.Calendar cal = calendarTableView.getSelectionModel().getSelectedItem();
        Model.getInstance().calendar_name = cal.getName();
        Model.getInstance().calendar_start = Integer.parseInt(cal.getStartYear());
        Model.getInstance().calendar_end = Integer.parseInt(cal.getEndYear());
        Model.getInstance().calendar_start_date = cal.getStartDate();
        
        // Load the calendar in the main window
        calendarGenerate();
    }

    @FXML
    private void pdfBtn(MouseEvent event) {
        exportCalendarPDF();
    }

    @FXML
    private void excelBtn(MouseEvent event) {
        exportCalendarExcel();
    }

    @FXML
    private void newRule(MouseEvent event) {
        newRuleEvent();
    }

    @FXML
    private void deleteRule(MouseEvent event) {
    }

    @FXML
    private void addSelectedRule(MouseEvent event) {
    }

    @FXML
    private void addAllRules(MouseEvent event) {
    }

    @FXML
    private void fileTabSelect(javafx.event.Event event) {
       // Load calendars
    }

    @FXML
    private void toolsTabSelect(javafx.event.Event event) {
        // Load colors
    }

    @FXML
    private void rulesTabSelect(javafx.event.Event event) {
        // Load rules
        
    }
}
