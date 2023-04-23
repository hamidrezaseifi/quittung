package de.seifi.rechnung_manager_app.ui;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.control.TextField;

public class FloatTextField extends TextField
{

    private FloatProperty valueProperty = new SimpleFloatProperty(0);

    public FloatTextField() {

        this.textProperty().addListener((obs, oldValue, newValue) -> {
            valueProperty.set(Float.parseFloat(newValue));

        });

        valueProperty.addListener((obs, oldValue, newValue) -> {
            setValue((Float) newValue);

        });
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        return text.matches("[0-9,.,\\,]*");
    }

    public Float getValue(){
    	try {
    		return Float.parseFloat(getText().replace(",", "."));
    	}
    	catch (NumberFormatException e) {
			setValue(0.0f);
			return 0.0f;
		}
        
    }

    public void setValue(Float value){
        valueProperty.setValue(value);
    	setText(String.valueOf(value));
    }

    public float getValueProperty() {
        return valueProperty.get();
    }

    public FloatProperty valuePropertyProperty() {
        return valueProperty;
    }

    public void setValueProperty(float valueProperty) {
        this.valueProperty.set(valueProperty);
    }
}
