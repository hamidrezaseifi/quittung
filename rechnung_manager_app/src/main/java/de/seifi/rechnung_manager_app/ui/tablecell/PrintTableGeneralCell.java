package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

public class PrintTableGeneralCell extends TableCell<RechnungItemPrintProperty, Object> {

    private final Pos cellAllignment;

    public PrintTableGeneralCell(Pos cellAllignment) {this.cellAllignment = cellAllignment;}


    protected void updateItem(Object item, boolean empty) {
        item = item == null? "" : item;

        super.updateItem(item, empty);
        this.setText(item.toString());
        this.setAlignment(cellAllignment);
        //this.setStyle("-fx-alignment: left;");
    }

}
