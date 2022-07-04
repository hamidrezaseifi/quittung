package de.seifi.quittung.db;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DbConnection {

	public static Connection getConnection() {

		String dbPathStr = getCurrentDbPath();
		
		String jdbcUrl = String.format("jdbc:sqlite:%s", dbPathStr);
		
        Connection conn = null;
        try {
            // db parameters
            String url = jdbcUrl;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            return conn;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        
        return null;
        
        /*finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
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
	
	public static void initialDbIfNotExists() {
		String dbPathStr = getCurrentDbPath();
		if(new File(dbPathStr).exists()) {
			return;
		}
		
		Connection conn = getConnection();
		
		try {
			Statement stmt = conn.createStatement();
			
			String quittung_sql = "CREATE TABLE quittung " +
	                "(id INTEGER PRIMARY KEY ASC, quittung_nummer VARCHAR(10), create_date VARCHAR(10), create_time VARCHAR(8))"; 
	
			stmt.executeUpdate(quittung_sql);
			
			String quittung_item_sql = "CREATE TABLE quittung_item " +
	                "(id INTEGER PRIMARY KEY ASC, quittung_id INT,  bezeichnung VARCHAR(255),  menge INT, preis FLOAT)"; 
	
			stmt.executeUpdate(quittung_item_sql);
			
		} catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
