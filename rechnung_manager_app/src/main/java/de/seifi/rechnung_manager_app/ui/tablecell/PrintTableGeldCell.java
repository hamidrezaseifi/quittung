package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

public class PrintTableGeldCell extends TableCell<RechnungItemPrintProperty, Float> {

    private final Pos cellAllignment;

    public PrintTableGeldCell(Pos cellAllignment) {this.cellAllignment = cellAllignment;}

    protected void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);
        String value = item != null ? TableUtils.formatGeld(item) : "";
        this.setText(value);
        this.setAlignment(cellAllignment);
        //this.setStyle("-fx-alignment: right;");
    }

}
