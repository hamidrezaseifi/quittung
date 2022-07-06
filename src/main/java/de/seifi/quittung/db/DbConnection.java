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

	private static Map<String, ISqliteRepository> mapTables = new HashMap<String, ISqliteRepository>();
	

	public static Connection getConnection() throws DataSqlException {

		String dbPathStr = getCurrentDbPath();
		
		String jdbcUrl = String.format("jdbc:sqlite:%s", dbPathStr);
		
        Connection conn = null;
        try {
            // db parameters
            String url = jdbcUrl;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            return conn;
            
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
		
		mapTables.put("quittung", new QuittungRepository());
		mapTables.put("quittung_item", new QuittungItemRepository());
		//mapTables.put("produkt", new QuittungRepository());
		
		Connection conn = getConnection();
		
		
		try {
			ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
			List<String> createdTables = new ArrayList<String>();
			while(rs.next()) {
				createdTables.add(rs.getString("TABLE_NAME"));
			}
			for(String key: mapTables.keySet()) {
				if(createdTables.contains(key) == false) {
					ISqliteRepository repo = mapTables.get(key);
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
