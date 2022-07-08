package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.ui.RechnungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProduktTableCellFactory implements
                                         Callback<TableColumn<RechnungItemProperty, String>, TableCell<RechnungItemProperty, String>> {
    @Override
    public TableCell<RechnungItemProperty, String> call(TableColumn<RechnungItemProperty, String> rechnungItemPropertyStringTableColumn) {
        return new ProduktTableCell();
    }
}
