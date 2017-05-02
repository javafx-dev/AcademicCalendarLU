package academiccalendar.ui.main;

import academiccalendar.model.DbCalendar;
import javafx.beans.property.SimpleStringProperty;

public class Calendar {
    private final SimpleStringProperty name;
    private final SimpleStringProperty startYear;
    private final SimpleStringProperty endYear;

    private final SimpleStringProperty startDate;

    private final DbCalendar dbCalendar;

    public Calendar(String name, String startYear, String endYear, String startingDate, DbCalendar calendar) {
        this.name = new SimpleStringProperty(name);
        this.startYear = new SimpleStringProperty(startYear);
        this.endYear = new SimpleStringProperty(endYear);
        this.startDate = new SimpleStringProperty(startingDate);
        this.dbCalendar = calendar;

    }


    public String getName() {
        return name.get();
    }

    public String getStartYear() {
        return startYear.get();
    }

    public String getEndYear() {
        return endYear.get();
    }

    public String getStartDate() {
        return startDate.get();
    }

    public DbCalendar getDbCalendar() {
        return dbCalendar;
    }
}
