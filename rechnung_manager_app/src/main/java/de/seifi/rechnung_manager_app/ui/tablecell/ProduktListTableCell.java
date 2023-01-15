package de.seifi.rechnung_manager_app.ui.tablecell;

import de.seifi.rechnung_manager_app.models.IReportLabelModel;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungReportItemModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class ProduktListTableCell<T extends IReportLabelModel> extends TableCell<RechnungReportItemModel, List<T>> {

    public ProduktListTableCell() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0));
        vBox.setSpacing(0);
        vBox.prefWidthProperty().bind(this.widthProperty().subtract(3));

        itemProperty().addListener((obx, oldItemList, newItemList) -> {
        	vBox.getChildren().clear();
            if (newItemList != null) {

               Color[] fxColors = {Color.rgb(242, 242, 255), Color.rgb(242, 255, 242)};

                for(int i=0;i<newItemList.size();i++) {
                    T item = newItemList.get(i);
                    Label lbl = new Label( item.getReportLabel());
                    lbl.setPadding(new Insets(5));
                    lbl.setBackground(new Background(new BackgroundFill(fxColors[i % fxColors.length], new CornerRadii(0), new Insets(0))));
                    lbl.setTextFill(Color.BLACK);
                    lbl.prefWidthProperty().bind(this.widthProperty().subtract(3));
                    vBox.getChildren().add(lbl);
                }

            }

        });

        setGraphic(vBox);
    }
}
