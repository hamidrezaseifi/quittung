package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.ui.IntegerTextField;
import javafx.scene.control.Control;

public class IntegerTableCell extends BaseEditbaleTableCell<Integer> {

    private IntegerTextField textField;
    
	@Override
	protected Control getEditingControl() {
		return textField;
	}

	@Override
	protected Integer getEditingControlValue() {
		return textField.getValue();
	}

	@Override
	protected void setEditingControlValue(Integer value) {
		value = value == null ? 0 : value;
		textField.setValue(value);
	}

	@Override
	protected void setCellText(Integer value) {
		if(value == null) {
			setText(String.valueOf(0));
		}
		else {
			setText(String.valueOf(value));
		}
		
		
	}

    public IntegerTableCell() {
    	super();

    }

	@Override
	protected void createEditingControl() {
		
		textField = new IntegerTextField();
	}


}
