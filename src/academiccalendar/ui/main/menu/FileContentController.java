/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.main.menu;

import academiccalendar.ui.main.FXMLDocumentController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class FileContentController implements Initializable {
    
    // Controller --------------------------------------------------------------
    private FXMLDocumentController mainController ;

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController ;
    }
    // -------------------------------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void newCalendar(MouseEvent event) {
        mainController.newCalendarEvent();
    }
    
}
