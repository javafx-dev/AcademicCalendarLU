/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

/**
 *
 * @author Karis
 */
public class EventEditor {
    
    static boolean saved;
    static String name;
    static String term;
    static String descript;
    
    public static void display() {
                
        // Set up pop-up window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Event");
        window.setMinWidth(250);
        
        // Labels
        Label subjectLbl = new Label();
        subjectLbl.setText("Name of Event:");
        Label termLbl = new Label();
        termLbl.setText("Choose Term:");
        Label descriptLbl = new Label();
        descriptLbl.setText("Description:");
        
        // Text fields
        TextField subjectField = new TextField();
        TextField termField = new TextField();
        TextField descriptField = new TextField();
        
        // Buttons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        
        // Save event information when user clicks save button and close window
        saveButton.setOnAction (e -> {
            saved = true;
            name = subjectField.getText();
            term = termField.getText();
            descript = descriptField.getText();
            window.close();
        });
        
        // Close window without saving changes
        cancelButton.setOnAction (e -> {
            window.close();
        });
        
        
        // Structure
        VBox layout = new VBox();
        
        HBox subjectCntrls = new HBox();
        subjectCntrls.getChildren().addAll(subjectLbl, subjectField);
        
        HBox termCntrls = new HBox();
        termCntrls.getChildren().addAll(termLbl, termField);
        
        HBox descriptCntrls = new HBox();
        descriptCntrls.getChildren().addAll(descriptLbl, descriptField);
        
        HBox buttonCntrls = new HBox();
        buttonCntrls.getChildren().addAll(saveButton, cancelButton);
        
        layout.getChildren().addAll(subjectCntrls, termCntrls,
        descriptCntrls, buttonCntrls);
        
        // Set and show scene
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }    
    
}
