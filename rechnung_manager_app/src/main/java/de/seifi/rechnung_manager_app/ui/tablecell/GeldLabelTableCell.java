package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.ui.TableUtils;

public class GeldLabelTableCell extends MarkedTableCellBase<Float> {

    public GeldLabelTableCell() {

        itemProperty().addListener((obx, oldItem, newItem) -> {
            String strValue = newItem != null? TableUtils.formatGeld(newItem) : "";
            setText(strValue);
        });
        
    }

    @Override
    public void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);

        
    }

}
