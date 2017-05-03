package academiccalendar.ui.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ColoredTerm {

    private final String name;
    private final String color;

    public ColoredTerm(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public StringProperty getName() {
        return new SimpleStringProperty(name);
    }

    public StringProperty getColor() {
        return new SimpleStringProperty(color);
    }


}
