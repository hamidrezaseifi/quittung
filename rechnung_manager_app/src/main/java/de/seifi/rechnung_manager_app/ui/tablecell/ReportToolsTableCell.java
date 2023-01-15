package de.seifi.rechnung_manager_app.ui.tablecell;

import java.util.Arrays;

import de.seifi.rechnung_manager_app.fx_services.ReportBindingService;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.models.ReportItemModel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class ReportToolsTableCell extends TableCell<ReportItemModel, String> {
	
	private final HBox hbox;
	private final Button btnEdit;
	private final Button btnPrint;
	
	public ReportToolsTableCell() {
		
		hbox = new HBox();
		hbox.setSpacing(2);
		
		btnPrint = new Button();
		btnPrint.setPadding(new Insets(0));
		btnPrint.getStyleClass().add("tools-print-button");
		btnPrint.setPrefWidth(35);
		btnPrint.setTooltip(new Tooltip("Rechnung Druken"));
		btnPrint.setPrefHeight(30);
		
		btnPrint.setOnAction((ActionEvent event) -> {

			PrintUtils.printRechnungItems(Arrays.asList(getCurrentRechnungModel()), false);
			
        });
		
		
		btnEdit = new Button();
		btnEdit.setPadding(new Insets(0));
		btnEdit.getStyleClass().add("tools-edit-button");
		btnEdit.setTooltip(new Tooltip("Rechnung Ansehen"));
		btnEdit.setPrefWidth(35);
		btnEdit.setPrefHeight(30);
		
		
		btnEdit.setOnAction((ActionEvent event) -> {
			
			RechnungModel model = getCurrentRechnungModel();
			UiUtils.showRechnungViewDialog(model);
			
        });

		
		hbox.getChildren().add(btnPrint);
		hbox.getChildren().add(btnEdit);
	}
	
	@Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(hbox);
        }
    }
	
	private RechnungModel getCurrentRechnungModel() {
		ReportBindingService reportBindingService = (ReportBindingService)this.getTableView().getUserData();
		
		ReportItemModel item = reportBindingService.getReportItems().get(this.getTableRow().getIndex());
		
		return item.getRechnungModel();
	}

}
