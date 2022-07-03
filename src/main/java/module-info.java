
module QuittungApplication {
    requires javafx.controls;
    requires javafx.fxml;

    opens de.seifi.quittung to javafx.fxml;
    opens de.seifi.quittung.controllers to javafx.fxml;

    exports de.seifi.quittung;
    exports de.seifi.quittung.ui;
    opens de.seifi.quittung.ui to javafx.fxml;
    exports de.seifi.quittung.ui.tablecell;
    opens de.seifi.quittung.ui.tablecell to javafx.fxml;
}
