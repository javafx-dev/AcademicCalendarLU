package academiccalendar.ui.listterms;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbTerm;
import academiccalendar.service.TermService;
import academiccalendar.ui.main.CustomFXMLLoader;
import academiccalendar.ui.main.FXMLDocumentController;
import academiccalendar.ui.main.Term;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
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

    @Autowired
    private CustomFXMLLoader loader;

    @Autowired
    private FXMLDocumentController mainController;

    @Autowired
    private TermService termService;

    // These fields are for mouse dragging of window
    private double xOffset;
    private double yOffset;

    private void editTermEvent() {

        if (!tableView.getSelectionModel().isEmpty()) {
            // Get selected term data
            Term term = tableView.getSelectionModel().getSelectedItem();

            // Store term data
            Model.getInstance().term_name = term.getTermName();
            Model.getInstance().term_date = term.getTermDate();

            // When user clicks on any date in the calendar, event editor window opens
            try {
                Parent rootLayout = loader.load(getClass().getClassLoader().getResource("edit_term.fxml"));
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

    public void initCol() {
        termCol.setCellValueFactory(new PropertyValueFactory<>("termName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("termDate"));
    }

    public void loadData() {
        ObservableList<Term> list = FXCollections.observableArrayList();
        // wipe current rules to add updates rules from database
        tableView.getItems().clear();
        List<DbTerm> dbTermList = termService.findAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

        for (DbTerm dbTerm : dbTermList) {
            list.add(new Term(dbTerm.getName(), dateFormat.format(dbTerm.getStartDate())));
        }
        tableView.getItems().setAll(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

    @FXML
    private void deleteTerm(MouseEvent event) {

        if (!tableView.getSelectionModel().isEmpty()) {
            //Show confirmation dialog to make sure the user want to delete the selected rule
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Term Deletion");
            alert.setContentText("WARNING!\nThis action cannot be undone!\nAre you sure you want to permanently delete this term?");
            //Customize the buttons in the confirmation dialog
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            //Set buttons onto the confirmation dialog
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            //Get the user's answer on whether deleting or not
            Optional<ButtonType> result = alert.showAndWait();

            //If the user wants to delete the rule, call the function that deletes the rule. Otherwise, close the window
            if (result.get() == buttonTypeYes) {
                deleteSelectedTerm();
            } else {
                // Close the window
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            }
        }
    }


    private void deleteSelectedTerm() {
        // Get selected term data
        Term term = tableView.getSelectionModel().getSelectedItem();
        String termName = term.getTermName();
        termService.deleteByName(termName);

        //Show message indicating that the selected term was deleted
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Term was successfully deleted");
        alertMessage.showAndWait();

        // Close the window, so that when user clicks on "Manage Your Terms" only the remaining existing terms appear
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }

}
