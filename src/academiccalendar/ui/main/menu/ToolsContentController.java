/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.main.menu;

import academiccalendar.ui.main.FXMLDocumentController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Karis
 */
public class ToolsContentController implements Initializable {

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
    
}