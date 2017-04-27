/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.listterms;

import academiccalendar.data.model.Model;
import academiccalendar.database.DBHandler;
import academiccalendar.ui.editrule.EditRuleController;
import academiccalendar.ui.editterm.EditTermController;
import academiccalendar.ui.main.FXMLDocumentController;
import academiccalendar.ui.main.Term;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ListTermsController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label topLabel;
    @FXML
    private TableView<Term> tableView;
    @FXML
    private TableColumn<Term, String> termCol;
    @FXML
    private TableColumn<Term, String> dateCol;
    
    // Main Controller -------------------------------
    private FXMLDocumentController mainController;
    
    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    // -------------------------------------------------------------------
    
    //--------------------------------------------------------------------
    //---------Database Object -------------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------
    
     ObservableList<Term> list = FXCollections.observableArrayList();
    
    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;
    
    private void editTermEvent() {
        
        if (!tableView.getSelectionModel().isEmpty()){
            // Get selected rule data
            Term term = tableView.getSelectionModel().getSelectedItem();

            // Store rule data
            Model.getInstance().term_name = term.getTermName();
            Model.getInstance().term_date = term.getTermDate();

            // When user clicks on any date in the calendar, event editor window opens
            try {
               // Load root layout from fxml file.
               FXMLLoader editTermLoader = new FXMLLoader();
               editTermLoader.setLocation(getClass().getResource("/academiccalendar/ui/editterm/edit_term.fxml"));
               AnchorPane rootLayout = (AnchorPane) editTermLoader.load();
               Stage stage = new Stage(StageStyle.UNDECORATED);
               stage.initModality(Modality.APPLICATION_MODAL); 

               EditTermController termController = editTermLoader.getController();
               termController.setListController(this);

               // Show the scene containing the root layout.
               Scene scene = new Scene(rootLayout);
               stage.setScene(scene);
               stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void initCol() {
        termCol.setCellValueFactory(new PropertyValueFactory<>("termName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("termDate"));
    }
    
    public void loadData(){
        
        // wipe current rules to add updates rules from database
        tableView.getItems().clear();
        list.clear();
        
        //Load all calendars into the Calendar View Table
        String qu = "SELECT * FROM TERMS";
        ResultSet result = databaseHandler.executeQuery(qu);
        
        try {
            while (result.next()) {
                String termName = result.getString("TermName");
                String termDate = result.getString("TermStartDate");
                
                list.add(new Term(termName, termDate));

               
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListTermsController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void editTerm(MouseEvent event) {
        editTermEvent();
    }
    
}
