package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.RechnungItemProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MarkedTableCellBase<T> extends TableCell<RechnungItemProperty, T> {
	private Background normalBackground = null; //new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	private final Background deletedBackground = new Background(new BackgroundFill(Color.rgb(255, 220, 220), CornerRadii.EMPTY, Insets.EMPTY));
	
	private ObjectProperty<Background> cellBackgroundProperty = null;
	
	private boolean isMarkedAsDeletedPropertySet = false;


    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if(cellBackgroundProperty == null) {
           	normalBackground = this.getBackground();

        	cellBackgroundProperty = new SimpleObjectProperty<>(normalBackground);
        }
    	
        this.backgroundProperty().bind(cellBackgroundProperty);
        
        
        if(this.getTableRow().getItem() != null && isMarkedAsDeletedPropertySet == false) {
        	this.getTableRow().getItem().getIsMarkedAsDeletedProperty().addListener((obx, oldItem, newItem) -> {
                if(newItem) {
                	cellBackgroundProperty.setValue(deletedBackground);
                }
                else {
                	cellBackgroundProperty.setValue(normalBackground);
                }
            });
        	isMarkedAsDeletedPropertySet = true;
        }

    }
}
