package de.seifi.rechnung_manager.ui;

import de.seifi.rechnung_manager.fx_services.RechnungBindingService;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

public class TableUtils {

    public static Pair<Integer, TableColumn<RechnungItemProperty,?>> findNextEditable(TableCell<RechnungItemProperty,?> cell){
        TableView<RechnungItemProperty> tv = cell.getTableView();
        int r = cell.getTableRow().getIndex();
        TableColumn<RechnungItemProperty,?> tc = cell.getTableColumn();
        int c = -1;
        for(int i=0; i< tv.getColumns().size(); i++){
            if(tv.getColumns().get(i) == tc){
                c = i;
                break;
            }
        }

        c += 1;
        while ((c >= tv.getColumns().size() - 1) || !(tv.getColumns().get(c)).isEditable()){
            r += 1;
            c = 0;
        }

        if(r >= tv.getItems().size()){
        	RechnungBindingService rechnungBindingService = (RechnungBindingService)tv.getUserData();
        	rechnungBindingService.addNewRow();
            
        }

        return new Pair<Integer, TableColumn<RechnungItemProperty, ?>>(r, tv.getColumns().get(c));
    }
    
    public static void selectNextEditable(TableCell<RechnungItemProperty,?> cell) {
    	TableView<RechnungItemProperty> tv = cell.getTableView();
    	RechnungBindingService rechnungBindingService = (RechnungBindingService)tv.getUserData();
    	if(rechnungBindingService.isCancelEditing()) {
    		return;
    	}
    	
    	Pair<Integer, TableColumn<RechnungItemProperty,?>> res = findNextEditable(cell);
    	
        if(res != null){
        	tv.getSelectionModel().select(res.getKey(), res.getValue());
        	tv.edit(res.getKey(), res.getValue());
        }
    }
    
    public static String formatGeld(Number geld) {
    	
    	String sGeld = String.format("%.2f \u20AC", geld);
    	
    	return sGeld;
    }
}
