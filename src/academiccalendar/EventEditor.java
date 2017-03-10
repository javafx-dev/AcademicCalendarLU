/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        window.setMinWidth(300);
        
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
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(saveButton, cancelButton);
               
        grid.add(subjectLbl, 0, 0);
        grid.add(subjectField, 1, 0);
        grid.add(termLbl, 0, 1);
        grid.add(termField, 1, 1);
        grid.add(descriptLbl, 0, 2);
        grid.add(descriptField, 1, 2);
        grid.add(buttons, 0, 3);
        
        // Alignments
        grid.setAlignment(Pos.CENTER);
        
        // Set and show scene
        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.show();
    }    
    
}
