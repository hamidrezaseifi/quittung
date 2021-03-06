package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungItemProperty;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import javafx.scene.control.TableCell;

public class GeldLabelTableCell extends TableCell<RechnungItemProperty, Float> {

    public GeldLabelTableCell() {

        itemProperty().addListener((obx, oldItem, newItem) -> {
            Float val = 0f;
            if (newItem != null) {
                val = newItem;
            }

            setText(TableUtils.formatGeld(val));
        });
        
    }

    @Override
    public void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);

    }

}
