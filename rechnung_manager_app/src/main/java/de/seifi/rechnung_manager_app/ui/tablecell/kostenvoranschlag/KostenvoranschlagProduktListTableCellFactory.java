package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.models.KostenvoranschlagReportItemModel;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagItemModel;
import de.seifi.rechnung_manager_app.models.RechnungReportItemModel;
import de.seifi.rechnung_manager_app.ui.tablecell.ProduktListTableCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.List;

public class KostenvoranschlagProduktListTableCellFactory implements
                                         Callback<TableColumn<KostenvoranschlagReportItemModel, List<KostenvoranschlagItemModel>>,
                                         			TableCell<KostenvoranschlagReportItemModel, List<KostenvoranschlagItemModel>>> {

    @Override
    public TableCell<KostenvoranschlagReportItemModel,
            List<KostenvoranschlagItemModel>> call(TableColumn<KostenvoranschlagReportItemModel,
            List<KostenvoranschlagItemModel>> produktListTableColumn) {
        return new KostenvoranschlagProduktListTableCell();
    }
}
