package de.seifi.rechnung_manager_app.ui;

import javafx.scene.control.TextField;

public class IntegerTextField extends TextField
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
        return text.matches("[0-9]*");
    }

    public Integer getValue(){
    	try {
    		return Integer.parseInt(getText());
    	}
    	catch (NumberFormatException e) {
			setValue(0);
			return 0;
		}
    }

    public void setValue(Integer value){
        setText(String.valueOf(value));
    }
}
