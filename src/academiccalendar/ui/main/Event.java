/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.ui.main;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Paul
 */
public class Event {
    private final SimpleStringProperty term;
    private final SimpleStringProperty subject;
    private final SimpleStringProperty date;
 
    public Event(String term, String subject, String date) {
        this.term = new SimpleStringProperty(term);
        this.subject = new SimpleStringProperty(subject);
        this.date = new SimpleStringProperty(date);
    }
    
    // Note: We need these accessors.
    public String getTerm() {
        return term.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getDate() {
        return date.get();
    }
}
