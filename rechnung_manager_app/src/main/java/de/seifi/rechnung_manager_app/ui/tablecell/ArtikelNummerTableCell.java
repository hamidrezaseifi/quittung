package de.seifi.rechnung_manager_app.ui.tablecell;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class ArtikelNummerTableCell extends BaseEditbaleTableCell<String> {

    private TextField textField;

    private StringConverter<String> converter = new DefaultStringConverter();


	@Override
	protected Control getEditingControl() {

		return textField;
	}

	@Override
	protected String getEditingControlValue() {
		return textField.getText();
	}

	@Override
	protected void setEditingControlValue(String value) {
		textField.setText(converter.toString(value));
	}

	@Override
	protected void setCellText(String text) {
		if (text == null) {
            setText(null);
        } else {
            setText(converter.toString(text));
        }
	}

	
    public ArtikelNummerTableCell() {
        super();

    }

	@Override
	protected void createEditingControl() {
		
		textField = new TextField();
	}

}
