
module QuittungApplication {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
    requires javafx.graphics;
	requires javafx.base;

    opens de.seifi.quittung to javafx.fxml;
    opens de.seifi.quittung.controllers to javafx.fxml;

    exports de.seifi.quittung;
    exports de.seifi.quittung.ui;
    exports de.seifi.quittung.db;
    exports de.seifi.quittung.ui.tablecell;

    opens de.seifi.quittung.db to org.sqlite;
    opens de.seifi.quittung.ui to javafx.fxml;
    opens de.seifi.quittung.ui.tablecell to javafx.fxml;
    exports de.seifi.quittung.db.base;
    opens de.seifi.quittung.db.base to org.sqlite;
}
