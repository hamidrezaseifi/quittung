package de.seifi.quittung.db.base;

import de.seifi.quittung.db.DbConnection;
import de.seifi.quittung.exception.DataSqlException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteRepositoryBase {

    protected Connection getConnection() throws DataSqlException {

        return DbConnection.getConnection();

    }
}
