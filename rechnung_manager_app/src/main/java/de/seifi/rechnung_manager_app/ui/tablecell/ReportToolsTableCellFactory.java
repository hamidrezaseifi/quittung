package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungReportItemModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ReportToolsTableCellFactory implements
                                         Callback<TableColumn<RechnungReportItemModel, String>, TableCell<RechnungReportItemModel, String>> {
    @Override
    public TableCell<RechnungReportItemModel, String> call(TableColumn<RechnungReportItemModel, String> tableColumn) {
        return new ReportToolsTableCell();
    }
}
