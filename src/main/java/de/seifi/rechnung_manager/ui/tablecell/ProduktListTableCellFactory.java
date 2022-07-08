package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.models.ReportItemModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProduktListTableCellFactory implements
                                         Callback<TableColumn<ReportItemModel, Object>, TableCell<ReportItemModel, Object>> {
    @Override
    public TableCell<ReportItemModel, Object> call(TableColumn<ReportItemModel, Object> produktListTableColumn) {
        return new ProduktListTableCell();
    }
}
