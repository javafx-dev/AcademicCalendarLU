package academiccalendar.ui.main;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbCalendar;
import academiccalendar.model.DbEvent;
import academiccalendar.model.DbTerm;
import academiccalendar.service.EventService;
import academiccalendar.service.TermService;
import academiccalendar.ui.controls.EventLabel;
import academiccalendar.ui.editevent.EditEventController;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Scope("singleton")
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

    // Center area
    @FXML
    private GridPane calendarGrid;
    @FXML
    private VBox centerArea;
    @FXML
    private ScrollPane scrollPane;


    @Autowired
    private CustomFXMLLoader loader;

    @Autowired
    private TermService termService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EditEventController editEventController;

    @FXML
    private VBox colorRootPane;
    // Color pickers
    @FXML
    private JFXColorPicker springSemCP;
    @FXML
    private JFXColorPicker fallSemCP;
    @FXML
    private JFXColorPicker allQtrCP;
    @FXML
    private JFXColorPicker allMbaCP;
    @FXML
    private JFXColorPicker allHalfCP;
    @FXML
    private JFXColorPicker allCampusCP;
    @FXML
    private JFXColorPicker allHolidayCP;

    // Events
    private void addEvent(VBox day) {

        // ONLY for days that have labels
        if (!day.getChildren().isEmpty()) {

            // Get the day label
            Label lbl = (Label) day.getChildren().get(0);

            // Store event day and month in data singleton
            Model.getInstance().event_day = Integer.parseInt(lbl.getText());
            Model.getInstance().event_month = Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());
            Model.getInstance().event_year = Integer.parseInt(selectedYear.getValue());

            // When user clicks on any date in the calendar, event editor window opens
            try {
                Parent rootLayout = loader.load(getClass().getClassLoader().getResource("add_event.fxml"));
                Stage stage = new Stage(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                // Show the scene containing the root layout.
                Scene scene = new Scene(rootLayout);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void editEvent(VBox day, String descript, String termID, DbEvent event) {

        // Store event day and month in data singleton
        Label dayLbl = (Label) day.getChildren().get(0);
        Model.getInstance().event_day = Integer.parseInt(dayLbl.getText());
        Model.getInstance().event_month = Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());
        Model.getInstance().event_year = Integer.parseInt(selectedYear.getValue());

        Model.getInstance().event_subject = descript;
        Model.getInstance().event_term_id = Integer.parseInt(termID);

        // When user clicks on any date in the calendar, event editor window opens
        try {
            // Load root layout from fxml file.
            Parent rootLayout = loader.load(getClass().getResource("/edit_event.fxml"));
            editEventController.setEvent(event);


            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void newCalendarEvent() {
        // When the user clicks "New Calendar" pop up window that let's them enter dates
        try {
            Parent rootLayout = loader.load(getClass().getClassLoader().getResource("add_calendar.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listCalendarsEvent() {
        // When the user clicks "New Calendar" pop up window that let's them enter dates
        try {

            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            Parent rootLayout = loader.load(getClass().getResource("/list_calendars.fxml"));
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void manageTermsEvent() {
        // When the user clicks "Manage Term Dates" pop up window that let's 
        // them change starting dates for terms
        try {

            Parent rootLayout = loader.load(getClass().getClassLoader().getResource("list_terms.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listRulesEvent() {
        // When the user clicks "Manage Rules" pop up window that let's them manage rules
        try {
            // Load root layout from fxml file.
            Parent rootLayout = loader.load(getClass().getClassLoader().getResource("list_rules.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

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
            Parent rootLayout = loader.load(getClass().getResource("/add_rule.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeMonthSelector() {
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

                    // Update the VIEWING MONTH
                    Model.getInstance().viewing_month = Model.getInstance().getMonthIndex(newValue);

                    repaintView();
                }

            }
        });

        // Add event listener to each month list item, allowing user to change months
        selectedYear.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue != null) {

                    // Update the VIEWING YEAR
                    Model.getInstance().viewing_year = Integer.parseInt(newValue);

                    repaintView();
                }
            }
        });
    }

    private void loadCalendarLabels() {

        // Get the current VIEW
        int year = Model.getInstance().viewing_year;
        int month = Model.getInstance().viewing_month;

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
        for (Node node : calendarGrid.getChildren()) {

            VBox day = (VBox) node;

            day.getChildren().clear();
            day.setStyle("-fx-backgroud-color: white");
            day.setStyle("-fx-font: 14px \"System\" ");

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

    public void calendarGenerate(DbCalendar dbCalendar) {
        Model.getInstance().calendar_id = dbCalendar.getId();

        // Load year selection
        selectedYear.getItems().clear(); // Invokes our change listener
        selectedYear.getItems().add(Integer.toString(dbCalendar.getStartYear()));
        selectedYear.getItems().add(Integer.toString(dbCalendar.getEndYear()));

        // Select the first YEAR as default     
        selectedYear.getSelectionModel().selectFirst();
        // Update the VIEWING YEAR
        Model.getInstance().viewing_year = Integer.parseInt(selectedYear.getSelectionModel().getSelectedItem());

        // Enable year selection box
        selectedYear.setVisible(true);

        // Get a list of all the months (1-12) in a year
        DateFormatSymbols dateFormat = new DateFormatSymbols();
        String months[] = dateFormat.getMonths();
        String[] spliceMonths = Arrays.copyOfRange(months, 0, 12);

        // Load month selection
        monthSelect.getItems().clear();  // Note, this will invoke our change listener too
        monthSelect.getItems().addAll(spliceMonths);

        // Select the first MONTH as default
        monthSelect.getSelectionModel().selectFirst();
        monthLabel.setText(monthSelect.getSelectionModel().getSelectedItem());
        // Update the VIEWING MONTH
        Model.getInstance().viewing_month =
                Model.getInstance().getMonthIndex(monthSelect.getSelectionModel().getSelectedItem());

        repaintView();
    }

    public void repaintView() {
        // Purpose - To be usable anywhere to update view
        // 1. Correct calendar labels based on Gregorian Calendar 
        // 2. Display events known to database

        loadCalendarLabels();
        populateMonthWithEvents();
    }

    private void populateMonthWithEvents() {

        Long calendarId = Model.getInstance().calendar_id;

        String currentMonth = monthLabel.getText();
        int currentMonthIndex = Model.getInstance().getMonthIndex(currentMonth) + 1;

        int currentYear = Integer.parseInt(selectedYear.getValue());

        List<DbEvent> eventList = eventService.findEventsByCalendarId(calendarId);
        for (DbEvent event : eventList) {
            LocalDate localDate = event.getDate();
            String eventDescript = event.getDescription();
            Long eventTermID = event.getTerm().getId();
            // Check for year we have selected
            if (currentYear == localDate.getYear()) {
                // Check for the month we already have selected (we are viewing)
                if (currentMonthIndex == localDate.getMonthValue()) {
                    // Get day for the month
                    int day = localDate.getDayOfMonth();

                    // Display decription of the event given it's day
                    showDate(day, eventDescript, eventTermID.intValue(), event);
                }
            }
        }
    }

    public void showDate(int dayNumber, String descript, int termID, DbEvent event) {

        Image img = new Image(getClass().getClassLoader().getResourceAsStream("icons/icon2.png"));
        ImageView imgView = new ImageView();
        imgView.setImage(img);

        for (Node node : calendarGrid.getChildren()) {

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
                    EventLabel eventLbl = new EventLabel(descript, event);
                    eventLbl.setGraphic(imgView);
                    eventLbl.getStyleClass().add("event-label");

                    // Save the term ID in accessible text
                    eventLbl.setAccessibleText(Integer.toString(termID));

                    eventLbl.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                        editEvent((VBox) eventLbl.getParent(), eventLbl.getText(), eventLbl.getAccessibleText(), event);

                    });

                    // Get term color from term's table
                    DbTerm dbTerm = termService.findOne((long) termID);
                    String eventRGB = dbTerm.getColor();
                    // Parse for rgb values
                    String[] colors = eventRGB.split("-");
                    String red = colors[0];
                    String green = colors[1];
                    String blue = colors[2];

                    eventLbl.setStyle("-fx-background-color: rgb(" + red +
                            ", " + green + ", " + blue + ", " + 1 + ");");

                    // Stretch to fill box
                    eventLbl.setMaxWidth(Double.MAX_VALUE);

                    // Mouse effects
                    eventLbl.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                        eventLbl.getScene().setCursor(Cursor.HAND);
                    });

                    eventLbl.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                        eventLbl.getScene().setCursor(Cursor.DEFAULT);
                    });

                    // Add label to calendar
                    day.getChildren().add(eventLbl);
                }
            }
        }
    }

    private void exportCalendarPDF() {
        TableView<Event> table = new TableView<>();
        ObservableList<Event> data = FXCollections.observableArrayList();


        double w = 500.00;
        // set width of table view
        table.setPrefWidth(w);
        // set resize policy
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // intialize columns
        TableColumn<Event, String> term = new TableColumn<>("Term");
        TableColumn<Event, String> subject = new TableColumn<>("Subject");
        TableColumn<Event, String> date = new TableColumn<>("Date");
        // set width of columns
        term.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 50% width
        subject.setMaxWidth(1f * Integer.MAX_VALUE * 60); // 50% width
        date.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 50% width
        // 
        term.setCellValueFactory(new PropertyValueFactory<>("term"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Add columns to the table
        table.getColumns().add(term);
        table.getColumns().add(subject);
        table.getColumns().add(date);

        Long calendarId = Model.getInstance().calendar_id;

        // Query to get ALL Events from the selected calendar!!
        List<DbEvent> events = eventService.findEventsByCalendarId(calendarId);
        for (DbEvent event : events) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String eventDate = df.format(event.getDate());
            data.add(new Event(event.getTerm().getName(), event.getDescription(), eventDate));
        }
        table.getItems().setAll(data);
        // open dialog window and export table as pdf
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.printPage(table);
            job.endJob();
        }
    }


    public void exportCalendarExcel() {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            createExcelSheet(file);
        }
    }

    private void createExcelSheet(File file) {
        Long calendarId = Model.getInstance().calendar_id;

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Calendar ID: " + calendarId);

        XSSFRow row = sheet.createRow(1);
        XSSFCell cell;

        cell = row.createCell(1);
        cell.setCellValue("Term");
        cell = row.createCell(2);
        cell.setCellValue("Subject");
        cell = row.createCell(3);
        cell.setCellValue("Date");

        List<DbEvent> events = eventService.findEventsByCalendarId(calendarId);
        int counter = 2;
        for (DbEvent event : events) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String eventDate = df.format(event.getDate());
            row = sheet.createRow(counter);
            cell = row.createCell(1);
            cell.setCellValue(event.getTerm().getName());
            cell = row.createCell(2);
            cell.setCellValue(event.getDescription());
            cell = row.createCell(3);
            cell.setCellValue(eventDate);
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }
            counter++;
        }

    }

    private String getRGB(Color c) {

        return Integer.toString((int) (c.getRed() * 255)) + "-"
                + Integer.toString((int) (c.getGreen() * 255)) + "-"
                + Integer.toString((int) (c.getBlue() * 255));
    }

    private void changeColors() {
        // Purpose - Update colors in database and calendar from color picker

        Color springSemColor = springSemCP.getValue();
        String springSemRGB = getRGB(springSemColor);
        termService.updateColorByTermName("SP SEM", springSemRGB);

        Color fallSemColor = fallSemCP.getValue();
        String fallSemRGB = getRGB(fallSemColor);
        termService.updateColorByTermName("FA SEM", fallSemRGB);

        Color allQtrColor = allQtrCP.getValue();
        String allQtrRGB = getRGB(allQtrColor);
        termService.updateColorByTermName("QTR", allQtrRGB);

        Color allMbaColor = allMbaCP.getValue();
        String allMbaRGB = getRGB(allMbaColor);
        termService.updateColorByTermName("MBA", allMbaRGB);

        Color allHalfColor = allHalfCP.getValue();
        String allHalfRGB = getRGB(allHalfColor);
        termService.updateColorByTermName("Half", allHalfRGB);

        Color allCampusColor = allCampusCP.getValue();
        String allCampusRGB = getRGB(allCampusColor);
        termService.updateColorByTermName("Campus", allCampusRGB);

        Color allHolidayColor = allHolidayCP.getValue();
        String allHolidayRGB = getRGB(allHolidayColor);
        termService.updateColorByTermName("Holiday", allHolidayRGB);

    }

    private void initalizeColorPicker() {

        String fallSemRGB = termService.findByName("FA SEM").getColor();
        String springSemRGB = termService.findByName("SP SEM").getColor();
        String mbaRGB = termService.findByName("FA I MBA").getColor();
        String qtrRGB = termService.findByName("FA QTR").getColor();
        String halfRGB = termService.findByName("FA 1st Half").getColor();
        String campusRGB = termService.findByName("Campus General").getColor();
        String holidayRGB = termService.findByName("Holiday").getColor();

        // Parse for rgb values for fall sem
        String[] colors = fallSemRGB.split("-");
        String red = colors[0];
        String green = colors[1];
        String blue = colors[2];
        Color c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        fallSemCP.setValue(c);

        // Parse for rgb values for spring sem
        colors = springSemRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        springSemCP.setValue(c);

        // Parse for rgb values for MBA
        colors = mbaRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        allMbaCP.setValue(c);

        // Parse for rgb values for QTR
        colors = qtrRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        allQtrCP.setValue(c);

        // Parse for rgb values for Half
        colors = halfRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        allHalfCP.setValue(c);

        // Parse for rgb values for Campus
        colors = campusRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        allCampusCP.setValue(c);

        // Parse for rgb values for Holiday
        colors = holidayRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        allHolidayCP.setValue(c);

    }

    public void initializeCalendarGrid() {

        // Go through each calendar grid location, or each "day" (7x6)
        int rows = 6;
        int cols = 7;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // Add VBox and style it
                VBox vPane = new VBox();
                vPane.getStyleClass().add("calendar_pane");
                vPane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

                vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    addEvent(vPane);
                });

                GridPane.setVgrow(vPane, Priority.ALWAYS);

                // Add it to the grid
                calendarGrid.add(vPane, j, i);
            }
        }

        // Set up Row Constraints
        for (int i = 0; i < 7; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(scrollPane.getHeight() / 7);
            calendarGrid.getRowConstraints().add(row);
        }
    }


    private void initializeCalendarWeekdayHeader() {

        // 7 days in a week
        int weekdays = 7;

        // Weekday names
        String[] weekAbbr = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (int i = 0; i < weekdays; i++) {

            // Make new pane and label of weekday
            StackPane pane = new StackPane();
            pane.getStyleClass().add("weekday-header");

            // Make panes take up equal space
            HBox.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);

            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            pane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

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
        initalizeColorPicker();
    }

    // Side - menu buttons 
    @FXML
    private void newCalendar(MouseEvent event) {
        newCalendarEvent();
    }

    @FXML
    private void manageCalendars(MouseEvent event) {
        listCalendarsEvent();
    }

    @FXML
    private void manageRules(MouseEvent event) {
        listRulesEvent();
    }

    @FXML
    private void newRule(MouseEvent event) {
        newRuleEvent();
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
    private void updateColors(MouseEvent event) {
        changeColors();

        if (Model.getInstance().calendar_id != null)
            repaintView();
    }

    @FXML
    private void manageTermDates(MouseEvent event) {
        manageTermsEvent();
    }


}
