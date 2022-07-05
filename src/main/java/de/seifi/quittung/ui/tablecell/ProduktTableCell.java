package de.seifi.quittung.ui.tablecell;

import de.seifi.quittung.ui.QuittungItemProperty;
import de.seifi.quittung.ui.TableUtils;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class ProduktTableCell extends TableCell<QuittungItemProperty, String> {

    //private final TextField textField = new TextField();

    private final ComboBox comboBox = new ComboBox();

    private StringConverter<String> converter = new DefaultStringConverter();

    public ProduktTableCell() {
        comboBox.setEditable(true);
        comboBox.prefWidthProperty().bind(this.widthProperty().subtract(3));

        itemProperty().addListener((obx, oldItem, newItem) -> {
            if (newItem == null) {
                setText(null);
            } else {
                setText(converter.toString(newItem));
            }
        });

        setGraphic(comboBox);
        setContentDisplay(ContentDisplay.TEXT_ONLY);

        comboBox.setOnAction(evt -> {
            commitEdit(this.converter.fromString(comboBox.getEditor().getText()));
        });
        comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(this.converter.fromString(comboBox.getEditor().getText()));
            }
        });

        comboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                comboBox.getEditor().setText(converter.toString(getItem()));
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
                commitEdit(comboBox.getEditor().getText());
                Pair<Integer, TableColumn> res = TableUtils.findNextEditable(this);
                if(res != null){
                    getTableView().getSelectionModel().select(res.getKey(), res.getValue());
                    getTableView().edit(res.getKey(), res.getValue());
                }

            }
        });

    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

    }

    @Override
    public void startEdit() {
        super.startEdit();
        comboBox.getEditor().setText(getItem());
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(String item) {
        super.commitEdit(item);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

}
