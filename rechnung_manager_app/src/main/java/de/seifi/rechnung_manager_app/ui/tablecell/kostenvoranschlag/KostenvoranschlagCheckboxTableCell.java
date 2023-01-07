package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.ui.FilterComboBox;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;

public class KostenvoranschlagCheckboxTableCell extends KostenvoranschlagBaseEditbaleTableCell<Boolean> {

    private CheckBox chkControl;

    public KostenvoranschlagCheckboxTableCell() {

        super();
        
    }

    @Override
    protected void createEditingControl() {
        chkControl = new CheckBox();

        chkControl.prefWidthProperty().bind(this.widthProperty().subtract(3));
    }

    @Override
    protected Control getEditingControl() {

        return chkControl;
    }

    @Override
    protected Boolean getEditingControlValue() {
        return chkControl.selectedProperty().getValue();
    }

    @Override
    protected void setEditingControlValue(Boolean value) {
        chkControl.setSelected(value);
    }

    @Override
    protected void setCellText(Boolean value) {
        value = value == null? false: value;

        chkControl.setSelected(value);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        
    }

}
