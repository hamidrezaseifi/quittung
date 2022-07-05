package de.seifi.quittung.ui.tablecell;

import de.seifi.quittung.ui.QuittungItemProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ArtikelNummerTableCellFactory implements
                                         Callback<TableColumn<QuittungItemProperty, String>, TableCell<QuittungItemProperty, String>> {
    @Override
    public TableCell<QuittungItemProperty, String> call(TableColumn<QuittungItemProperty, String> quittungItemPropertyStringTableColumn) {
        return new ArtikelNummerTableCell();
    }
}
