package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.ui.FloatTextField;
import de.seifi.rechnung_manager.ui.IntegerTextField;
import de.seifi.rechnung_manager.ui.RechnungItemProperty;
import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class BaseTableCell<T> extends TableCell<RechnungItemProperty, T> {

	protected abstract void createEditingControl();

	protected abstract Control getEditingControl();

	protected abstract T getEditingControlValue();

	protected abstract void setEditingControlValue(T value);

	protected abstract void setCellText(T text);


    public BaseTableCell() {
        
    	createEditingControl();
    	
        itemProperty().addListener((obx, oldItem, newItem) -> {
            if (newItem == null) {
                setCellText(null);
            } else {
            	setCellText(newItem);
            }
        });

        setGraphic(getEditingControl());
        setContentDisplay(ContentDisplay.TEXT_ONLY);

        /*textField.setOnAction(evt -> {
            commitEdit(this.converter.fromString(textField.getText()));
        });
        
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(this.converter.fromString(textField.getText()));
            }
        });*/
        
        getEditingControl().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                if((getEditingControl() instanceof TextField) || 
                		(getEditingControl() instanceof IntegerTextField) ||
                		(getEditingControl() instanceof FloatTextField)) {
                	
                	TextField txtField = (TextField)getEditingControl();
                	txtField.selectAll();
                }
            }
            else {
            	commitEdit(getEditingControlValue());
            }
        });

        getEditingControl().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
            	setEditingControlValue(getItem());
                cancelEdit();
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT) {
                getTableView().getSelectionModel().selectRightCell();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                getTableView().getSelectionModel().selectLeftCell();
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                getTableView().getSelectionModel().selectAboveCell();
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                getTableView().getSelectionModel().selectBelowCell();
                event.consume();
            }else if (event.getCode() == KeyCode.TAB) {
                commitEdit(getEditingControlValue());
            }
            
            if (((event.getCode() == KeyCode.TAB) && !event.isShiftDown()) || (event.getCode() == KeyCode.ENTER)) {
            	TableUtils.selectNextEditable(this);
            }
            if ((event.getCode() == KeyCode.TAB) && event.isShiftDown()) {
            	if(TableUtils.selectPrevEditable(this)) {
            		
            	}
            	else {
            		event.consume();
            	}
            }
            
        });

    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

    }

    @Override
    public void startEdit() {
        super.startEdit();
        setEditingControlValue(getItem());
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        getEditingControl().requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(T item) {
        super.commitEdit(item);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
        
    }

}
