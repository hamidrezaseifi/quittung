package de.seifi.quittung.exception;

import java.sql.SQLException;

public class DataSqlException extends SQLException {

    public DataSqlException(String reason) {
        super(reason);
    }
}
