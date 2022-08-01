package de.seifi.rechnung_manager_module.ui.tablecell;

import de.seifi.rechnung_manager_module.models.RechnungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class IntegerTableCellFactory implements
                                         Callback<TableColumn<RechnungItemProperty, Integer>, TableCell<RechnungItemProperty, Integer>> {
    @Override
    public TableCell<RechnungItemProperty, Integer> call(TableColumn<RechnungItemProperty, Integer> rechnungItemPropertyStringTableColumn) {
        return new IntegerTableCell();
    }
}
