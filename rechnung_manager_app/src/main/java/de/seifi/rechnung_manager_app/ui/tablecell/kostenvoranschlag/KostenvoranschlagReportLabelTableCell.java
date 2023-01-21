package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class KostenvoranschlagReportLabelTableCell extends KostenvoranschlagReportBaseTableCell<String> {

    private StringConverter<String> converter = new DefaultStringConverter();


	@Override
	protected void createLabelControl() {

	}

	@Override
	protected void setCellText(String value) {
		setText(value);
	}
}
