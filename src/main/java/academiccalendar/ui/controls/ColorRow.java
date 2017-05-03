package academiccalendar.ui.controls;

import academiccalendar.model.DbColorGroup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXColorPicker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ColorRow extends HBox {

    private final DbColorGroup colorGroup;
    private final JFXColorPicker picker = new JFXColorPicker();
    public ColorRow(DbColorGroup colorGroup) {
        this.colorGroup = colorGroup;
        createColorView();

    }

    private void createColorView() {
        this.setPadding(new Insets(0, 30, 0, 0));
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setPrefWidth(304.0);
        this.setPrefHeight(43.0);
        this.setSpacing(20);
        Label label = new Label(colorGroup.getName() + ":");
        HBox.setMargin(label, new Insets(0, 5, 0, 0));
        VBox.setMargin(this, new Insets(10, 0, 0, 0));
        this.getChildren().add(label);

        picker.setPrefWidth(50.0);
        picker.setPrefHeight(24.0);
        picker.setPromptText("Pick Color");
        picker.setEditable(true);
        String[] colors = colorGroup.getColor().split("-");
        String red = colors[0];
        String green = colors[1];
        String blue = colors[2];
        Color c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        picker.setValue(c);

        this.getChildren().add(picker);
        JFXCheckBox checkbox = new JFXCheckBox("visible ?");
        checkbox.setSelected(true);
        checkbox.setCheckedColor(Color.web("#777777"));
        this.getChildren().add(checkbox);
    }

    public DbColorGroup getColorGroup() {
        return colorGroup;
    }

    public Color getCurrentColor() {
        return picker.getValue();
    }
}
