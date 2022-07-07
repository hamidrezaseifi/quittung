package de.seifi.rechnung_manager.ui.tablecell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.seifi.rechnung_manager.fx_services.QuittungBindingService;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.ui.FilterComboBox;
import de.seifi.rechnung_manager.ui.QuittungItemProperty;
import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class ProduktTableCell extends TableCell<QuittungItemProperty, String> {

    private final FilterComboBox comboBox;
    private ObservableList<String> obsProduktList;
    
    private StringConverter<String> converter = new DefaultStringConverter();

    public ProduktTableCell() {
     	
    	comboBox = new FilterComboBox(FXCollections.observableArrayList());
    	
    	reloadProdukts();
    	
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
        reloadProdukts();
    }
    
    private void reloadProdukts() {
        comboBox.setItems(obsProduktList);
        List<ProduktModel> produktList = QuittungBindingService.CURRENT_INSTANCE.getProduktList();
    	
    	obsProduktList = FXCollections.observableArrayList(produktList.stream().map(p -> p.getProduktName()).collect(Collectors.toList()));

    }

    @Override
    public void startEdit() {
        super.startEdit();
        comboBox.getEditor().setText(getItem());
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        comboBox.requestFocus();
        comboBox.show();
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
