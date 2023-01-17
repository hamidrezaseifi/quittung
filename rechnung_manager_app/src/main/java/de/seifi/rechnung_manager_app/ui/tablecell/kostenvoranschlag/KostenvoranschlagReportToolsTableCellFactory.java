package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.models.KostenvoranschlagReportItemModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class KostenvoranschlagReportToolsTableCellFactory implements
                                         Callback<TableColumn<KostenvoranschlagReportItemModel, String>, TableCell<KostenvoranschlagReportItemModel, String>> {
    @Override
    public TableCell<KostenvoranschlagReportItemModel, String> call(TableColumn<KostenvoranschlagReportItemModel, String> tableColumn) {
        return new KostenvoranschlagReportToolsTableCell();
    }
}
