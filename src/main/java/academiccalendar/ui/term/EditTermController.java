package academiccalendar.ui.term;

import academiccalendar.data.model.Model;
import academiccalendar.model.DbColorGroup;
import academiccalendar.service.ColorGroupService;
import academiccalendar.service.TermService;
import academiccalendar.ui.common.AbstractDraggableController;
import academiccalendar.ui.main.FXMLDocumentController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


@Component
public class EditTermController extends AbstractDraggableController {

    @Autowired
    private TermService termService;

    @Autowired
    private ListTermsController listController;

    @Autowired
    private ColorGroupService colorGroupService;

    @Autowired
    private FXMLDocumentController mainController;

    @FXML
    private Label termLabel;
    @FXML
    private JFXDatePicker termDatePicker;

    @FXML
    private JFXComboBox<DbColorGroup> colorSelector;

    private ObservableList<DbColorGroup> colorGroupList;

    private void autofill() {

        // Retrieve term name
        String name = Model.getInstance().term_name;
        //Set name to TermLabel. show it ot the user
        termLabel.setText(name);

        //Retrieve date of start date selected term
        String termStartDate = Model.getInstance().term_date;
        String[] termDateParts = termStartDate.split("-");
        int year = Integer.parseInt(termDateParts[0]);
        int month = Integer.parseInt(termDateParts[1]);
        int day = Integer.parseInt(termDateParts[2]);

        // Set default value for datepicker
        termDatePicker.setValue(LocalDate.of(year, month, day));

        colorGroupList = FXCollections.observableArrayList(colorGroupService.findAll());

        colorSelector.setConverter(new StringConverter<DbColorGroup>() {
            @Override
            public String toString(DbColorGroup object) {
                return object.getName();
            }

            @Override
            public DbColorGroup fromString(String string) {
                return colorGroupList.stream().filter((s) -> s.getName().equals(string)).findFirst().get();
            }
        });

        Callback cellFactory = new Callback<ListView<DbColorGroup>, ListCell<DbColorGroup>>() {
            @Override
            public ListCell<DbColorGroup> call(ListView<DbColorGroup> l) {
                return new ListCell<DbColorGroup>() {
                    @Override
                    protected void updateItem(DbColorGroup item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId() + "    " + item.getName());
                        }
                    }
                };
            }
        };

        colorSelector.setButtonCell((ListCell) cellFactory.call(null));
        colorSelector.setCellFactory(cellFactory);
        colorSelector.setItems(colorGroupList);


    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        autofill();
        super.initialize(url, rb);
    }

    @FXML
    private void exit(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void update(MouseEvent event) {

        updateTermDate();

    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


    private void updateTermDate() {

        //Get the name of the term to be updated and it current starting date
        String termName = Model.getInstance().term_name;

        //Check if user actually selected a new starting date
        if (termDatePicker.getValue() == null) {
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please select new starting date for the term");
            alertMessage.showAndWait();
            return;
        }
        DbColorGroup newColorGroup = colorSelector.getValue();
        if (colorSelector.getValue() == null){
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please select color group");
            alertMessage.showAndWait();
            return;
        }
        termService.updateTerm(termName, termDatePicker.getValue(),newColorGroup.getId());
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText("Term was updated successfully");
        alertMessage.showAndWait();

        // Update list of terms in the table view to show new starting date
        listController.loadData();
        mainController.repaintView();
        // Close the window
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }

}
