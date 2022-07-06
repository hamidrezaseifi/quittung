package de.seifi.quittung.db;

import de.seifi.quittung.db.base.ISqliteRepository;
import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.models.QuittungItemModel;
import de.seifi.quittung.models.QuittungModel;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbConnection {

	private static List<ISqliteRepository> mapTables = new ArrayList<ISqliteRepository>();
	
	private static Connection lastConnection = null;

	public static Connection getConnection() throws DataSqlException {
		
		String dbPathStr = getCurrentDbPath();
		
		String jdbcUrl = String.format("jdbc:sqlite:%s", dbPathStr);
		
        try {
    		if(lastConnection != null) {
    			if(lastConnection.isClosed() == false) {
    				return lastConnection;
    			}
    		}
    			
            String url = jdbcUrl;
            // create a connection to the database
            lastConnection = DriverManager.getConnection(url);
            
            return lastConnection;
            
        } catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler bei der Verbindung mit Datenbank: %s", ex.getMessage()));
        } 

    }
	
	public static String getWorkingRootPath() {
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString();
		
		return path;
	}
	
	public static String getCurrentDbPath() {
		String rootPath = getWorkingRootPath();
		Path dbPath = Path.of(rootPath, "quittung.db");
		String dbPathStr = dbPath.toAbsolutePath().toString();
		dbPathStr = dbPathStr.replace("\\", "/");
		
		return dbPathStr;
	}
	
	public static void initialDbIfNotExists() throws DataSqlException {
		
		mapTables.add(new QuittungRepository());
		mapTables.add(new QuittungItemRepository());
		mapTables.add(new ProduktRepository());
		
		Connection conn = getConnection();
		
		
		try {
			ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
			List<String> createdTables = new ArrayList<String>();
			while(rs.next()) {
				createdTables.add(rs.getString("TABLE_NAME"));
			}
			for(ISqliteRepository repo: mapTables) {
				if(createdTables.contains(repo.getTableName()) == false) {
					
					repo.createTable(conn);
				}
				
			}
			
		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Erstellen von Datenbank: %s", ex.getMessage()));
        }
		finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
		}
      
	}


}
