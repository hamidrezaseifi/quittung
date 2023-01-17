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

    private BooleanProperty isReport = new SimpleBooleanProperty(false);

    public boolean isIsReport() {
        return this.isReport.get();
    }

    public BooleanProperty isReportProperty() {

        return isReport;
    }

    public void setIsReport(boolean isReport) {
        this.isReport.set(isReport);
    }


    @Override
    public TableCell<KostenvoranschlagReportItemModel,
            List<KostenvoranschlagItemModel>> call(TableColumn<KostenvoranschlagReportItemModel,
            List<KostenvoranschlagItemModel>> produktListTableColumn) {
        return new ProduktListTableCell(isIsReport());
    }
}
