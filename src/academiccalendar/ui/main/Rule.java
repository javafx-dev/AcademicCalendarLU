/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.main;


import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author RodolfoPC
 */
public class Rule {
    private final SimpleStringProperty eventDescription;
        private final SimpleStringProperty termID;
        private final SimpleStringProperty daysFromStart;
         
        public Rule(String eventSubject, String auxTermID, String days) {
            this.eventDescription = new SimpleStringProperty(eventSubject);
            this.termID = new SimpleStringProperty(auxTermID);
            this.daysFromStart = new SimpleStringProperty(days);
        }
         
        public String getEventDescription() {
            return eventDescription.get();
        }

        public String getTermID() {
            return termID.get();
        }

        public String getDaysFromStart() {
            return daysFromStart.get();
        }
        
}
