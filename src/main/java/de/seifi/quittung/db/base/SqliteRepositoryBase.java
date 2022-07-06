package de.seifi.quittung.db.base;

import de.seifi.quittung.db.DbConnection;
import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.models.QuittungModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class SqliteRepositoryBase {

    protected Connection getConnection() throws DataSqlException {

        return DbConnection.getConnection();

    }
    
    protected abstract String getCreateSql();
    
    protected abstract String getTableName();
    
	public void createTable(Connection connection) throws DataSqlException {
		Statement stmt;
		try {
			
			stmt = connection.createStatement();
			stmt.executeUpdate(getCreateSql());
			
		} catch (Exception ex) {
			throw new DataSqlException(String.format("Fehler beim Erstellen von der Tabelle '%s': %s", getTableName(), ex.getMessage()));
		}
		
		
	}


}
