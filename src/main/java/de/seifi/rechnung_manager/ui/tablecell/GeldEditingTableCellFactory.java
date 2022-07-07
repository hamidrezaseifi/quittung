package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.ui.QuittungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class GeldEditingTableCellFactory implements
                                         Callback<TableColumn<QuittungItemProperty, Float>, TableCell<QuittungItemProperty, Float>> {
    @Override
    public TableCell<QuittungItemProperty, Float> call(TableColumn<QuittungItemProperty, Float> quittungItemPropertyStringTableColumn) {
        return new GeldEditingTableCell();
    }
}