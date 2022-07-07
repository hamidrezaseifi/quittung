package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.ui.QuittungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProduktTableCellFactory implements
                                         Callback<TableColumn<QuittungItemProperty, String>, TableCell<QuittungItemProperty, String>> {
    @Override
    public TableCell<QuittungItemProperty, String> call(TableColumn<QuittungItemProperty, String> quittungItemPropertyStringTableColumn) {
        return new ProduktTableCell();
    }
}
