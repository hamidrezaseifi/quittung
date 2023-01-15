package de.seifi.rechnung_manager_app.ui.tablecell.kostenvoranschlag;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class KostenvoranschlagCheckboxTableCell extends KostenvoranschlagBaseEditbaleTableCell<Boolean> {

    private CheckBox chkControl;

    private HBox showHox = new HBox();

    private VBox showVbox = new VBox();

    private ImageView imageView;

    private Image okImage;

    private Image noImage;

    public KostenvoranschlagCheckboxTableCell() {
        super();

        showHox = new HBox();
        showVbox = new VBox();
        okImage = new Image(RechnungManagerFxApp.class.getResource("images/ok.png").toExternalForm());
        noImage = new Image(RechnungManagerFxApp.class.getResource("images/cancel.png").toExternalForm());


        showHox.setSpacing(10);

        imageView = new ImageView();
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);

        showHox.getChildren().addAll(imageView, showVbox);


    }

    @Override
    protected void createEditingControl() {
        chkControl = new CheckBox();

        chkControl.prefWidthProperty().bind(this.widthProperty().subtract(3));
    }

    @Override
    protected Control getEditingControl() {

        return chkControl;
    }

    @Override
    protected Boolean getEditingControlValue() {
        return chkControl.selectedProperty().getValue();
    }

    @Override
    protected void setEditingControlValue(Boolean value) {
        chkControl.setSelected(value);
    }

    @Override
    protected void setCellText(Boolean value) {
        value = value == null? false: value;

        chkControl.setSelected(value);

        if(value == null) {
            setText("");
        }
        else {
            setText(value? "✓": "X");
        }
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        /*if (!isEditing() && showHox != null && imageView != null && item != null) {
            imageView.setImage(item? okImage: noImage);

            setGraphic(showHox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        else{
            Control control = getEditingControl();
            setGraphic(control);
            setContentDisplay(ContentDisplay.TEXT_ONLY);

        }*/
    }

}
