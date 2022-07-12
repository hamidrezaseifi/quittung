package de.seifi.rechnung_manager.ui.tablecell;

import java.io.IOException;
import java.util.Arrays;

import de.seifi.rechnung_manager.RechnungManagerFxApp;
import de.seifi.rechnung_manager.controllers.PrintDialogController;
import de.seifi.rechnung_manager.fx_services.RechnungBindingService;
import de.seifi.rechnung_manager.fx_services.ReportBindingService;
import de.seifi.rechnung_manager.models.RechnungItemProperty;
import de.seifi.rechnung_manager.models.ReportItemModel;
import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

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
		btnPrint.setPrefHeight(30);
		
		btnPrint.setOnAction((ActionEvent event) -> {
			
			ReportBindingService reportBindingService = (ReportBindingService)this.getTableView().getUserData();
			
			ReportItemModel item = reportBindingService.getReportItems().get(this.getTableRow().getIndex());
			
			
			try {
				Pair<Parent, FXMLLoader> pair = RechnungManagerFxApp.loadFXMLLoader("rechnung_print");
				GridPane printPane = (GridPane)pair.getKey();
		        FXMLLoader fxmlLoader = pair.getValue();
		        PrintDialogController dialogController = fxmlLoader.<PrintDialogController>getController();
		        dialogController.printRechnungList(Arrays.asList(item.getRechnungModel()));
			} catch (IOException e) {
				
			}
	        
        });
		
		
		btnEdit = new Button();
		btnEdit.setPadding(new Insets(0));
		btnEdit.getStyleClass().add("tools-edit-button");
		btnEdit.setPrefWidth(35);
		btnEdit.setPrefHeight(30);
		
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

}