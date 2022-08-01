package de.seifi.rechnung_manager_module.ui.tablecell;

import de.seifi.rechnung_manager_module.models.RechnungItemProperty;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProduktTableCellFactory implements
                                     Callback<TableColumn<RechnungItemProperty, String>, ProduktTableCell> {
    @Override
    public ProduktTableCell call(TableColumn<RechnungItemProperty, String> rechnungItemPropertyStringTableColumn) {
        return new ProduktTableCell();
    }
}
