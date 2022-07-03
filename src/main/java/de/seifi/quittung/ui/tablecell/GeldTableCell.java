package de.seifi.quittung.ui.tablecell;

import de.seifi.quittung.ui.FloatTextField;
import de.seifi.quittung.ui.IntegerTextField;
import de.seifi.quittung.ui.QuittungItemProperty;
import de.seifi.quittung.ui.TableUtils;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class GeldTableCell extends TableCell<QuittungItemProperty, Float> {

    private final FloatTextField textField = new FloatTextField();
    private final Label lblPresent = new Label();

    public GeldTableCell() {

        itemProperty().addListener((obx, oldItem, newItem) -> {
            Float val = 0f;
            if (newItem != null) {
                val = newItem;
            }

            setText(String.valueOf(newItem) + " €");
        });

        setGraphic(textField);
        setContentDisplay(ContentDisplay.TEXT_ONLY);

        textField.setOnAction(evt -> {
            commitEdit(textField.getValue());
        });
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(textField.getValue());
            }
        });

        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                textField.setText(String.valueOf(getItem()));
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
            }
            else if (event.getCode() == KeyCode.TAB) {
                commitEdit(textField.getValue());
                Pair<Integer, TableColumn> res = TableUtils.findNextEditable(this);
                if(res != null){
                    getTableView().getSelectionModel().select(res.getKey(), res.getValue());
                    getTableView().edit(res.getKey(), res.getValue());
                }
            }
        });

    }

    @Override
    public void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);

    }

    @Override
    public void startEdit() {
        super.startEdit();
        textField.setText(String.valueOf(getItem()));
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.selectAll();
        textField.requestFocus();

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(Float item) {
        super.commitEdit(item);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

}
