package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.ui.TableUtils;
import de.seifi.rechnung_manager_app.ui.tablecell.MarkedTableCellBase;

public class KostenvoranschlagGeldLabelTableCell extends KostenvoranschlagMarkedTableCellBase<Float> {

    public KostenvoranschlagGeldLabelTableCell() {

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
