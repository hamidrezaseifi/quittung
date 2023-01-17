package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class KostenvoranschlagPropertyValueFactory<S,T> extends PropertyValueFactory<S,T> {
    /**
     * Creates a default PropertyValueFactory to extract the value from a given
     * TableView row item reflectively, using the given property name.
     *
     * @param property The name of the property with which to attempt to
     *                 reflectively extract a corresponding value for in a given object.
     */
    public KostenvoranschlagPropertyValueFactory(String property) {
        super(property);
    }

    @Override
    public TableCell<S,T> cell
}
