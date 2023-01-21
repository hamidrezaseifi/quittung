package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
import de.seifi.rechnung_manager_app.fx_services.KostenvoranschlagReportBindingService;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagModel;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagReportItemModel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
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

public class KostenvoranschlagReportToolsTableCell extends KostenvoranschlagReportBaseTableCell<String>  {
	
	private final HBox hbox;
	private final Button btnEdit;
	private final Button btnDone;
	private final Button btnPrint;

	@Override
	protected void createLabelControl() {

	}

	@Override
	protected void setCellText(String value) {

	}

	public KostenvoranschlagReportToolsTableCell() {

		hbox = new HBox();
		hbox.setSpacing(2);
		
		btnPrint = new Button();
		btnPrint.setPadding(new Insets(0));
		btnPrint.getStyleClass().add("tools-print-button");
		btnPrint.setPrefWidth(35);
		btnPrint.setTooltip(new Tooltip("Kostenvoranschlag Druken"));
		btnPrint.setPrefHeight(30);
		
		btnPrint.setOnAction((ActionEvent event) -> {

			PrintUtils.printKostenvoranschlagItems(Arrays.asList(getCurrentRechnungModel()));
			
        });


		btnEdit = new Button();
		btnEdit.setPadding(new Insets(0));
		btnEdit.getStyleClass().add("tools-edit-button");
		btnEdit.setTooltip(new Tooltip("Kostenvoranschlag Ansehen"));
		btnEdit.setPrefWidth(35);
		btnEdit.setPrefHeight(30);


		btnEdit.setOnAction((ActionEvent event) -> {

			KostenvoranschlagModel model = getCurrentRechnungModel();
			UiUtils.showKostenvoranschlagViewDialog(model);

		});

		btnDone = new Button();
		btnDone.setPadding(new Insets(0));
		btnDone.getStyleClass().add("tools-done-button");
		btnDone.setTooltip(new Tooltip("Kostenvoranschlag fertig machen"));
		btnDone.setPrefWidth(35);
		btnDone.setPrefHeight(30);


		btnDone.setOnAction((ActionEvent event) -> {

			KostenvoranschlagModel model = getCurrentRechnungModel();

			KostenvoranschlagReportBindingService
					reportBindingService = (KostenvoranschlagReportBindingService)this.getTableView().getUserData();

			reportBindingService.makeKostenvoranschlagDone(model);
			reportBindingService.setItemAt(this.getTableRow().getIndex(), model);

		});

		
		hbox.getChildren().add(btnPrint);
		hbox.getChildren().add(btnDone);
		hbox.getChildren().add(btnEdit);
	}
	
	@Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(hbox);
			KostenvoranschlagModel model = getCurrentRechnungModel();
			if(model.getStatus().getValue() >= 10 && hbox.getChildren().size() >= 2){
				hbox.getChildren().remove(btnDone);
				hbox.getChildren().remove(btnEdit);
			}

        }
    }


}
