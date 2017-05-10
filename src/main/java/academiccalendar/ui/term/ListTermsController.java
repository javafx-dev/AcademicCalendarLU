package academiccalendar.ui.term;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbTerm;
import academiccalendar.service.TermService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.main.ColoredTerm;
import academiccalendar.ui.main.CustomFXMLLoader;
import academiccalendar.ui.main.FXMLDocumentController;
import academiccalendar.ui.main.Term;
import academiccalendar.utils.ColorConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
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
public class ListTermsController extends AbstractDraggableController {

    @FXML
    private TableView<Term> tableView;
    @FXML
    private TableColumn<Term, ColoredTerm> termCol;
    @FXML
    private TableColumn<Term, String> dateCol;

    @Autowired
    private CustomFXMLLoader loader;

    @Autowired
    private TermService termService;


    private void editTermEvent() {

        if (!tableView.getSelectionModel().isEmpty()) {
            // Get selected term data
            Term term = tableView.getSelectionModel().getSelectedItem();

            // Store term data
            Model.getInstance().termName = term.getTermName();
            Model.getInstance().termDate = term.getTermDate();

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

    private void initCol() {
        termCol.setCellValueFactory(term -> term.getValue().getColoredTerm());
        dateCol.setCellValueFactory(new PropertyValueFactory<>("termDate"));

        termCol.setCellFactory(getTableColumnTableCellCallback());
    }

    private Callback<TableColumn<Term, ColoredTerm>, TableCell<Term, ColoredTerm>> getTableColumnTableCellCallback() {
        return column -> new TableCell<Term, ColoredTerm>() {


            @Override
            protected void updateItem(ColoredTerm item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    // Format date.
                    setText(item.getName().getValue());
                    String[] items = item.getColor().getValue().split("-");
                    String color = "rgb(" + items[0] + "," + items[1] + "," + items[2] + ")";
                    setTextFill(Color.web(color));
                    setStyle("-fx-text-color: " + ColorConverter.toRGBCode(Color.web(color)));
                }
            }
        };
    }

    void loadData() {
        ObservableList<Term> list = FXCollections.observableArrayList();
        // wipe current rules to add updates rules from database
        tableView.getItems().clear();
        List<DbTerm> dbTermList = termService.findAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

        for (DbTerm dbTerm : dbTermList) {
            list.add(new Term(dbTerm.getName(), dateFormat.format(dbTerm.getStartDate()), new ColoredTerm(dbTerm.getName(), dbTerm.getColorGroup().getColor())));
        }
        tableView.getItems().setAll(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initCol();
        loadData();
        super.initialize(url, rb);
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
            ButtonType buttonTypeYes = ButtonType.YES;
            ButtonType buttonTypeNo = ButtonType.NO;
            //Set buttons onto the confirmation dialog
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            //Get the user's answer on whether deleting or not
            Optional<ButtonType> result = alert.showAndWait();

            //If the user wants to delete the rule, call the function that deletes the rule. Otherwise, close the window
            if (result.isPresent() && result.get() == buttonTypeYes) {
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
