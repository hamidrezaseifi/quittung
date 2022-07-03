package de.seifi.quittung.ui;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

public class TableUtils {

    public static Pair<Integer, TableColumn> findNextEditable(TableCell cell){
        TableView tv = cell.getTableView();
        int r = cell.getTableRow().getIndex();
        TableColumn tc = cell.getTableColumn();
        int c = -1;
        for(int i=0; i< tv.getColumns().size(); i++){
            if(tv.getColumns().get(i) == tc){
                c = i;
                break;
            }
        }

        c += 1;
        while ((c >= tv.getColumns().size() - 1) || !((TableColumn)tv.getColumns().get(c)).isEditable()){
            r += 1;
            c = 0;
        }

        if(r >= tv.getItems().size()){
            return null;
        }

        return new Pair(r, tv.getColumns().get(c));
    }
}
