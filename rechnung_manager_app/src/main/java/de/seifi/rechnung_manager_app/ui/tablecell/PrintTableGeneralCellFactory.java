package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class PrintTableGeneralCellFactory implements
                                         Callback<TableColumn<RechnungItemPrintProperty, Object>, TableCell<RechnungItemPrintProperty, Object>> {

    private Pos cellAllignment;

    @Override
    public TableCell<RechnungItemPrintProperty, Object> call(TableColumn<RechnungItemPrintProperty, Object> rechnungItemPrintPropertyStringTableColumn) {
        return new PrintTableGeneralCell(cellAllignment);
    }

    public Pos getCellAllignment() {
        return cellAllignment;
    }

    public void setCellAllignment(Pos cellAllignment) {
        this.cellAllignment = cellAllignment;
    }
}
