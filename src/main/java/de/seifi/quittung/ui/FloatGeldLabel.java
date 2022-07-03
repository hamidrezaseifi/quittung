package de.seifi.quittung.ui;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FloatGeldLabel extends Label
{
    private FloatProperty value = new SimpleFloatProperty(0);

    public FloatGeldLabel() {
        this.setAlignment(Pos.CENTER_RIGHT);
        this.value.addListener((data, before, after) -> {

            super.setText(String.valueOf(after) + " €");
        });
    }

    public float getValue() {
        return value.get();
    }

    public FloatProperty valueProperty() {
        return value;
    }

    public void setValue(float value) {
        this.value.set(value);
    }
}
