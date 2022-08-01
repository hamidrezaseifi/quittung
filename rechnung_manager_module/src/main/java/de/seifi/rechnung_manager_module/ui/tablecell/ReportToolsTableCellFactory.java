package de.seifi.rechnung_manager_module.ui.tablecell;

import de.seifi.rechnung_manager_module.models.ReportItemModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ReportToolsTableCellFactory implements
                                         Callback<TableColumn<ReportItemModel, String>, TableCell<ReportItemModel, String>> {
    @Override
    public TableCell<ReportItemModel, String> call(TableColumn<ReportItemModel, String> tableColumn) {
        return new ReportToolsTableCell();
    }
}
