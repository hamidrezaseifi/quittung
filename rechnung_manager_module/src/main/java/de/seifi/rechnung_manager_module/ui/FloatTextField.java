package de.seifi.rechnung_manager_module.ui;

import javafx.scene.control.TextField;

public class FloatTextField extends TextField
{
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
    	setText(String.valueOf(value));
    }
}
