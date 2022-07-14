package de.seifi.rechnung_manager_app.ui.tablecell;

import java.util.List;
import java.util.stream.Collectors;

import de.seifi.rechnung_manager_app.fx_services.RechnungBindingService;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.ui.FilterComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class ProduktTableCell extends BaseEditbaleTableCell<String> {

    private FilterComboBox editComboBox;
    private ObservableList<String> obsProduktList;
    
    private StringConverter<String> converter = new DefaultStringConverter();


	@Override
	protected Control getEditingControl() {

		return editComboBox;
	}

	@Override
	protected String getEditingControlValue() {
		return editComboBox.getEditor().getText();
	}

	@Override
	protected void setEditingControlValue(String value) {
		editComboBox.getEditor().setText(converter.toString(value));
	}

	@Override
	protected void setCellText(String text) {
		if (text == null) {
            setText(null);
        } else {
            setText(converter.toString(text));
        }
	}

	
    public ProduktTableCell() {
        
        super();
     	 
    }
    
    private void reloadProdukts() {
        editComboBox.setItems(obsProduktList);
        List<ProduktModel> produktList = RechnungBindingService.CURRENT_INSTANCE.getProduktList();
    	
    	obsProduktList = FXCollections.observableArrayList(produktList.stream().map(p -> p.getProduktName()).collect(Collectors.toList()));

    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        reloadProdukts();
    }

    @Override
    public void startEdit() {
        super.startEdit();
        editComboBox.show();
    }

	@Override
	protected void createEditingControl() {
    	editComboBox = new FilterComboBox(FXCollections.observableArrayList());
    	
    	reloadProdukts();
    	
        editComboBox.setEditable(true);
        editComboBox.prefWidthProperty().bind(this.widthProperty().subtract(3));

		
	}

}
