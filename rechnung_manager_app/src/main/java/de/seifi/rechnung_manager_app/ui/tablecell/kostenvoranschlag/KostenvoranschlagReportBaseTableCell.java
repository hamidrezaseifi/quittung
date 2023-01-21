package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
import de.seifi.rechnung_manager_app.fx_services.KostenvoranschlagReportBindingService;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagModel;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagReportItemModel;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Arrays;

public abstract class KostenvoranschlagReportBaseTableCell<T> extends TableCell<KostenvoranschlagReportItemModel, T> {


	private Background doneBackground = new Background(new BackgroundFill(Color.rgb(255, 220, 220), CornerRadii.EMPTY, Insets.EMPTY));
	private Background activeBackground = new Background(new BackgroundFill(Color.rgb(220, 255, 220), CornerRadii.EMPTY, Insets.EMPTY));


	protected abstract void createLabelControl();

	protected abstract void setCellText(T value);

	public KostenvoranschlagReportBaseTableCell() {
		createLabelControl();

		itemProperty().addListener((obx, oldItem, newItem) -> {
			if (newItem == null) {
				setCellText(null);
			} else {
				setCellText(newItem);
			}
		});
	}

	@Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            KostenvoranschlagModel model = getCurrentRechnungModel();

			if(model.getStatus() == KostenvoranschlagStatus.DONE){
				this.setBackground(doneBackground);
			}

			if(model.getStatus() == KostenvoranschlagStatus.ACTIVE){
				this.setBackground(activeBackground);
			}
        }
    }

	protected KostenvoranschlagModel getCurrentRechnungModel() {
		KostenvoranschlagReportBindingService
				reportBindingService = (KostenvoranschlagReportBindingService)this.getTableView().getUserData();

		KostenvoranschlagReportItemModel item = reportBindingService.getReportItems().get(this.getTableRow().getIndex());

		return item.getKostenvoranschlagModel();
	}

}
