package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class PrintTableGeldCellFactory implements
                                       Callback<TableColumn<RechnungItemPrintProperty, Float>, TableCell<RechnungItemPrintProperty, Float>> {
    private Pos cellAllignment;

    public Pos getCellAllignment() {
        return cellAllignment;
    }

    public void setCellAllignment(Pos cellAllignment) {
        this.cellAllignment = cellAllignment;
    }

    @Override
    public TableCell<RechnungItemPrintProperty, Float> call(TableColumn<RechnungItemPrintProperty, Float> rechnungItemPrintPropertyFloatTableColumn) {
        return new PrintTableGeldCell(cellAllignment);
    }
}
