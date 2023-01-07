package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.models.KostenvoranschlagItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class KostenvoranschlagProduktTableCellFactory implements
                                         Callback<TableColumn<KostenvoranschlagItemProperty, String>, TableCell<KostenvoranschlagItemProperty, String>> {
    @Override
    public TableCell<KostenvoranschlagItemProperty, String> call(TableColumn<KostenvoranschlagItemProperty, String> itemPropertyStringTableColumn) {
        return new KostenvoranschlagProduktTableCell();
    }
}
