package academiccalendar.ui.main;


import javafx.beans.property.SimpleStringProperty;

public class Rule {
    private final SimpleStringProperty eventDescription;
    private final SimpleStringProperty termID;
    private final SimpleStringProperty daysFromStart;
    private final Long dbRuleId;

    public Rule(String eventSubject, String auxTermID, String days, Long dbRuleId) {
        this.eventDescription = new SimpleStringProperty(eventSubject);
        this.termID = new SimpleStringProperty(auxTermID);
        this.daysFromStart = new SimpleStringProperty(days);
        this.dbRuleId = dbRuleId;

    }

    public Long getDbRuleId() {
        return dbRuleId;
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
