package de.seifi.rechnung_manager_module.ui.tablecell;

import de.seifi.rechnung_manager_module.models.RechnungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ArtikelNummerTableCellFactory implements
                                         Callback<TableColumn<RechnungItemProperty, String>, TableCell<RechnungItemProperty, String>> {
    @Override
    public TableCell<RechnungItemProperty, String> call(TableColumn<RechnungItemProperty, String> rechnungItemPropertyStringTableColumn) {
        return new ArtikelNummerTableCell();
    }
}
