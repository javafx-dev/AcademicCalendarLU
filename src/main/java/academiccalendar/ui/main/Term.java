package academiccalendar.ui.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Term {
    private final SimpleStringProperty termName;
    private final SimpleStringProperty termDate;
    private final ColoredTerm coloredTerm;

    public Term(String termName, String termDate, ColoredTerm coloredTerm) {
        this.termName = new SimpleStringProperty(termName);
        this.termDate = new SimpleStringProperty(termDate);
        this.coloredTerm = coloredTerm;
    }

    public String getTermName() {
        return termName.get();
    }

    public String getTermDate() {
        return termDate.get();
    }

    public ObjectProperty<ColoredTerm> getColoredTerm() {
        return new SimpleObjectProperty<>(coloredTerm) ;
    }
}
