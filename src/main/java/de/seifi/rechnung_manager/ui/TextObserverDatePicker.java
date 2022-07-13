package de.seifi.rechnung_manager.ui;

import java.time.LocalDate;

import javafx.scene.control.DatePicker;

public class TextObserverDatePicker extends DatePicker {

	public TextObserverDatePicker() {
		super();

		initialize();
	}

	public TextObserverDatePicker(LocalDate localDate) {
		super(localDate);

		initialize();
	}
	
	private void initialize() {
		focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
    		if (!isNowFocused) {
				try {
					LocalDate date = getConverter().fromString(getEditor().getText());
					if(date != null){
						setValue(date);
					}
					else {
						getEditor().setText(getConverter().toString(getValue()));
					}
				}
				catch (Exception ex){
					getEditor().setText(getConverter().toString(getValue()));
				}

            }
    		
        });

	}

}
