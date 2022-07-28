package de.seifi.rechnung_manager_app.ui.tablecell;

import java.util.List;

import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.ReportItemModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProduktListTableCellFactory implements
                                         Callback<TableColumn<ReportItemModel, List<RechnungItemModel>>, 
                                         			TableCell<ReportItemModel, List<RechnungItemModel>>> {
    @Override
    public TableCell<ReportItemModel, List<RechnungItemModel>> call(TableColumn<ReportItemModel, List<RechnungItemModel>> produktListTableColumn) {
        return new ProduktListTableCell();
    }
}
