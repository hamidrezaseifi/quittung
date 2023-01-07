package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.models.KostenvoranschlagItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class KostenvoranschlagCheckboxTableCellFactory implements
                                         Callback<TableColumn<KostenvoranschlagItemProperty, Boolean>, TableCell<KostenvoranschlagItemProperty, Boolean>> {
    @Override
    public TableCell<KostenvoranschlagItemProperty, Boolean> call(TableColumn<KostenvoranschlagItemProperty, Boolean> itemPropertyStringTableColumn) {
        return new KostenvoranschlagCheckboxTableCell();
    }
}
