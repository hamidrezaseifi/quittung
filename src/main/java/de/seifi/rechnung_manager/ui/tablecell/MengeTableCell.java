package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.ui.IntegerTextField;
import javafx.scene.control.Control;

public class MengeTableCell extends BaseTableCell<Integer> {

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

    public MengeTableCell() {
    	super();

    }

	@Override
	protected void createEditingControl() {
		
		textField = new IntegerTextField();
	}


}
