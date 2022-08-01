package de.seifi.rechnung_manager_app.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FilterComboBox extends ComboBox<String> {

	public FilterComboBox(ObservableList<String> items) {
		super();
		
		FilteredList<String> filteredItems = new FilteredList<String>(items, p -> true);
		
		getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = getEditor();
            final String selected = getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {
                
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                    	if(newValue == null) {
                    		return false;
                    	}
                        if (item.toUpperCase().contains(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });
		
		setItems(filteredItems);
	}

	
}
