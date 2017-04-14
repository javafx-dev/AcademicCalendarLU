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
        private final SimpleStringProperty termName;
        private final SimpleStringProperty daysFromStart;
         
        public Rule(String eventSubject, String nameOfTerm, String days) {
            this.eventDescription = new SimpleStringProperty(eventSubject);
            this.termName = new SimpleStringProperty(nameOfTerm);
            this.daysFromStart = new SimpleStringProperty(days);
        }
         
        public String getEventDescription() {
            return eventDescription.get();
        }

        public String getTermNameOfRule() {
            return termName.get();
        }

        public String getDaysFromStart() {
            return daysFromStart.get();
        }
        
}
