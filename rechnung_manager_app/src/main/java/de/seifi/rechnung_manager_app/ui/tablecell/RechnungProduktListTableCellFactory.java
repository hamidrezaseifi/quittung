package de.seifi.rechnung_manager_app.ui.tablecell;

import java.util.List;

import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungReportItemModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class RechnungProduktListTableCellFactory implements
                                         Callback<TableColumn<RechnungReportItemModel, List<RechnungItemModel>>,
                                         			TableCell<RechnungReportItemModel, List<RechnungItemModel>>> {
    @Override
    public TableCell<RechnungReportItemModel, List<RechnungItemModel>> call(TableColumn<RechnungReportItemModel, List<RechnungItemModel>> produktListTableColumn) {
        return new ProduktListTableCell();
    }
}
