package de.seifi.rechnung_manager.ui.tablecell;

import de.seifi.rechnung_manager.models.RechnungItemModel;
import de.seifi.rechnung_manager.models.ReportItemModel;
import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProduktListTableCell extends TableCell<ReportItemModel, Object> {

    public ProduktListTableCell() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0));
        vBox.setSpacing(0);
        vBox.prefWidthProperty().bind(this.widthProperty().subtract(3));

        itemProperty().addListener((obx, oldItem, newItem) -> {
        	vBox.getChildren().clear();
            if (newItem != null) {

                List<RechnungItemModel> list = (List<RechnungItemModel>)newItem;
                String[] colors = {"#f2f2ff", "#f2fff2"};
                Color[] fxColors = {Color.rgb(242, 242, 255), Color.rgb(242, 255, 242)};

                for(int i=0;i<list.size();i++) {
                    RechnungItemModel item = list.get(i);
                    Label lbl = new Label(item.getProdukt() + ": " + item.getArtikelNummer());
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
