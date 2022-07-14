package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class GeldLabelTableCellFactory implements
                                         Callback<TableColumn<RechnungItemProperty, Float>, TableCell<RechnungItemProperty, Float>> {
    @Override
    public TableCell<RechnungItemProperty, Float> call(TableColumn<RechnungItemProperty, Float> rechnungItemPropertyStringTableColumn) {
        return new GeldLabelTableCell();
    }
}
