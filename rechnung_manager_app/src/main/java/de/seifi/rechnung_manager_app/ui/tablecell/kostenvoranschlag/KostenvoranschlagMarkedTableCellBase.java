package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.models.KostenvoranschlagItemProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class KostenvoranschlagMarkedTableCellBase<T> extends TableCell<KostenvoranschlagItemProperty, T> {
	private Background normalBackground = null; //new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	private final Background bestelltBackground = new Background(new BackgroundFill(Color.rgb(220, 255, 220), CornerRadii.EMPTY, Insets.EMPTY));
	
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
        	this.getTableRow().getItem().bestelltProperty().addListener((obx, oldItem, newItem) -> {
                if(newItem) {
                	cellBackgroundProperty.setValue(bestelltBackground);
                }
                else {
                	cellBackgroundProperty.setValue(normalBackground);
                }
            });
        	isMarkedAsDeletedPropertySet = true;
        }

    }
}
