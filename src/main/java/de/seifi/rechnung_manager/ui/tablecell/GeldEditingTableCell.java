package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.ui.FloatTextField;
import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.scene.control.Control;

public class GeldEditingTableCell extends BaseTableCell<Float> {

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
		textField.setValue(value);
		
	}

	@Override
	protected void setCellText(Float value) {
		
		if(value == null) {
			setText(TableUtils.formatGeld(0.0f));
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
