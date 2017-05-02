package academiccalendar.ui.controls;

import academiccalendar.model.DbEvent;
import javafx.scene.control.Label;

public class EventLabel extends Label {


    private final DbEvent event;

    public EventLabel(String text, DbEvent event) {
        super(text);
        this.event = event;
    }

    public DbEvent getEvent() {
        return event;
    }
}
