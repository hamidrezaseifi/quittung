package de.seifi.rechnung_manager_app.ui;

import de.seifi.rechnung_manager_app.fx_services.IBindingService;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

public class TableUtils {

    public static <T> Pair<Integer, TableColumn<T,?>> findNextEditable(TableCell<T,?> cell){
        TableView<T> tv = cell.getTableView();
        
        Pair<Integer, Integer> pos = getTableCellPosition(cell);
        
        int r = pos.getKey();
        int c = pos.getValue();

        c += 1;

        while ((c > tv.getColumns().size() - 1) || !tv.getColumns().get(c).isEditable()){
            r += 1;
            c = 0;
        }

        if(r >= tv.getItems().size()){
            IBindingService bindingService = (IBindingService)tv.getUserData();
        	if(bindingService.isEditingMode() == false) {
                bindingService.addNewRow();
        	}
        	else {
        		return null;
        	}
        	
            
        }

        return new Pair<Integer, TableColumn<T, ?>>(r, tv.getColumns().get(c));
    }
    
    public static<T> Pair<Integer, TableColumn<T,?>> findPrevEditable(TableCell<T,?> cell){
        TableView<T> tv = cell.getTableView();
        
        Pair<Integer, Integer> pos = getTableCellPosition(cell);
        
        int r = pos.getKey();
        int c = pos.getValue();
        
        
        if(c == 0 && r == 0) {
        	return null;
        }
        
        c -= 1;
        if(c < 0) {
        	r -= 1;
        	c = tv.getColumns().size() - 1;
        }
        
        while ((c >= tv.getColumns().size() - 1) || !(tv.getColumns().get(c)).isEditable()){
        	c -= 1;
        	if(c < 0) {
            	r -= 1;
            	c = tv.getColumns().size() - 1;
            }
        	if(c <= 0 && r <= 0) {
            	return null;
            }
        }

        if(r >= tv.getItems().size()){
            IBindingService bindingService = (IBindingService)tv.getUserData();
            bindingService.addNewRow();
            
        }

        return new Pair<Integer, TableColumn<T, ?>>(r, tv.getColumns().get(c));
    }
    
    public static Pair<Integer, Integer> getTableCellPosition(TableCell<?,?> cell){
    	TableView<?> tv = cell.getTableView();
        int r = cell.getTableRow().getIndex();
        TableColumn<?,?> tc = cell.getTableColumn();
        int c = -1;
        for(int i=0; i< tv.getColumns().size(); i++){
            if(tv.getColumns().get(i) == tc){
                c = i;
                break;
            }
        }
        
        return new Pair<Integer, Integer>(r, c);
    }
    
    public static <T> void selectNextEditable(TableCell<T,?> cell) {
     	TableView<T> tv = cell.getTableView();
    	Pair<Integer, TableColumn<T,?>> res = findNextEditable(cell);
    	
        if(res != null){
        	tv.getSelectionModel().select(res.getKey(), res.getValue());
        	tv.edit(res.getKey(), res.getValue());
        }
    }
    
    public static <T> boolean selectPrevEditable(TableCell<T,?> cell) {
    	TableView<T> tv = cell.getTableView();
    	Pair<Integer, TableColumn<T,?>> res = findPrevEditable(cell);
    	
        if(res != null){
        	tv.getSelectionModel().select(res.getKey(), res.getValue());
        	tv.edit(res.getKey(), res.getValue());
        	return true;
        }
        return false;	
    }
    
    
    
    
    public static String formatGeld(Number geld) {
    	
    	String sGeld = String.format("%.2f \u20AC", geld);
    	
    	return sGeld;
    }
}
