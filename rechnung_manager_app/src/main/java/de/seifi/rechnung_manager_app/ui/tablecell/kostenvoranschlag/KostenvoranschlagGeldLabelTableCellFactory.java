package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.models.KostenvoranschlagItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class KostenvoranschlagGeldLabelTableCellFactory implements
                                         Callback<TableColumn<KostenvoranschlagItemProperty, Float>, TableCell<KostenvoranschlagItemProperty, Float>> {
    @Override
    public TableCell<KostenvoranschlagItemProperty, Float> call(TableColumn<KostenvoranschlagItemProperty, Float> rechnungItemPropertyStringTableColumn) {
        return new KostenvoranschlagGeldLabelTableCell();
    }
}
