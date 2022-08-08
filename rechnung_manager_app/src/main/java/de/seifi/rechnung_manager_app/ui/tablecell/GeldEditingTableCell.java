package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.ui.FloatTextField;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import javafx.scene.control.Control;

public class GeldEditingTableCell extends BaseEditbaleTableCell<Float> {

    private FloatTextField textField;


	@Override
	protected Control getEditingControl() {
		return textField;
	}

	@Override
	protected Float getEditingControlValue() {
		
		return textField.getValue();
	}

	@Override
	protected void setEditingControlValue(Float value) {
		value = value == null ? 0.0f : value;
		textField.setValue(value);
		
	}

	@Override
	protected void setCellText(Float value) {
		
		if(value == null) {
			setText("");
		}
		else {
			setText(TableUtils.formatGeld(value));
		}
	}

	
    public GeldEditingTableCell() {
    	super();

    }

	@Override
	protected void createEditingControl() {
		textField = new FloatTextField();
		
	}


}
