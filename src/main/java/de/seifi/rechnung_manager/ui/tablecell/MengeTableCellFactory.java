package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.ui.QuittungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class MengeTableCellFactory implements
                                         Callback<TableColumn<QuittungItemProperty, Integer>, TableCell<QuittungItemProperty, Integer>> {
    @Override
    public TableCell<QuittungItemProperty, Integer> call(TableColumn<QuittungItemProperty, Integer> quittungItemPropertyStringTableColumn) {
        return new MengeTableCell();
    }
}
