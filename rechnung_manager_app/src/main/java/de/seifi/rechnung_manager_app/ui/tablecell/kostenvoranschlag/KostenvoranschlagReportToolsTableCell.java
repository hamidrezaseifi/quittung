package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.fx_services.KostenvoranschlagReportBindingService;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagModel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Optional;

public class KostenvoranschlagReportToolsTableCell extends KostenvoranschlagReportBaseTableCell<String>  {
	
	private final HBox hbox;
	private final Button btnView;
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


		btnView = new Button();
		btnView.setPadding(new Insets(0));
		btnView.getStyleClass().add("tools-edit-button");
		btnView.setTooltip(new Tooltip("Kostenvoranschlag Ansehen"));
		btnView.setPrefWidth(35);
		btnView.setPrefHeight(30);


		btnView.setOnAction((ActionEvent event) -> {

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

			Optional<ButtonType> result =  UiUtils.showAsking("Fertig die Kostenvoranschlag ...",
															  "Soll die Kostenvoranschlag fertig werden?");
			if(result.isPresent()){
				if(result.get() == ButtonType.OK){
					reportBindingService.makeKostenvoranschlagDone(model);
					reportBindingService.setItemAt(this.getTableRow().getIndex(), model);
				}

			}

		});

		
		hbox.getChildren().add(btnPrint);
		hbox.getChildren().add(btnDone);
		hbox.getChildren().add(btnView);
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
				hbox.getChildren().remove(btnView);
			}

        }
    }


}
