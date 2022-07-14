package de.seifi.rechnung_common.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfigUtils {
	
	private static final String CONFIG_KEY_DATABASE_SECTOR = "database";


	//private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_URL = "quittung.datasource.url";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER = "server";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_PORT = "port";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE = "database";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD = "password";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME = "username";

	
	//private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/rechnung";

	private static final String CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_SERVER = "localhost";

	private static final String CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_PORT = "5432";

	private static final String CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_DATABASE = "rechnung";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_PASSWORD = "7342";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_USERNAME = "postgres";
	

	private static final List<String> existingTables = Arrays.asList("produkt_last_presi", "rechnung", "rechnung_item");
	
	private String datasourceUrl;
	
	private String datasourceUsername;
	
	private String datasourcePassword;
	
	@Value("${quittung.datasource.driver-class-name}")
	private String datasourceDriverClassName;
	
	
	@Bean
    public DataSource dataSource() {
		
		readDatabaseConfig();
		
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(datasourceDriverClassName);
        dataSource.setJdbcUrl(datasourceUrl);
        dataSource.setUsername(datasourceUsername);
        dataSource.setPassword(datasourcePassword);
        

        return dataSource;
    }
	
    public boolean testConnection(String driver, String server, String port, String database, String username, String password) throws SQLException, RuntimeException {
		
		String dbUrl = createDatasourceUrl(server, port, database);
		
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
        DatabaseMetaData mData = dataSource.getConnection().getMetaData();
        ResultSet rs = mData.getTables(null, null, "%", null);
        
        List<String> checkingTables = new ArrayList<String>();
        checkingTables.addAll(existingTables);
        while (rs.next()) {
        	if(rs.getString("TABLE_TYPE")!= null && rs.getString("TABLE_TYPE").toLowerCase().equals("table")) {
        		String tableName = rs.getString("TABLE_NAME");
        		System.out.println(tableName);
        		if(checkingTables.contains(tableName)) {
        			checkingTables.remove(tableName);
        		}
        	}
          
        }
        
        dataSource.close();
        
        if(checkingTables.isEmpty() == false) {
        	throw new RuntimeException(String.format("Die '%s' Tabellen würde nicht gefünden!", checkingTables));
        }
        return true;
    }

	
    public boolean saveConnection(File configFile, String server, String port, String database, String username, String password) throws IOException {
    	if(configFile.exists() == false) {
    		configFile.createNewFile();
    	}
    	
		
		Ini ini = new Ini(configFile);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, server);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, port);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, database);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, username);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, password);
		
		ini.store();
		
		return true;
    }

	public boolean readDatabaseConfig() {
		File configFile = getConfigIniFile();
		
		try {
			if(!configFile.exists()) {
				initializeConfig(configFile);
			}
			readConfig(configFile);
			
			return true;

		} catch (IOException e) {
			
			e.printStackTrace();
			
			return false;
		}
	}
	
	private void initializeConfig(File configFile) throws InvalidFileFormatException, IOException {
		
		saveConnection(configFile, 
				CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_SERVER,
				CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_PORT,
				CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_DATABASE,
				CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_USERNAME,
				CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_PASSWORD);
		
	}
	
	private void readConfig(File configFile) throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(configFile);
		String server = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, String.class);
		String port = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, String.class);
		String database = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, String.class);
		this.datasourceUrl = createDatasourceUrl(server, port, database);
		
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_SERVER);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_PORT);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_DATABASE);

		
		this.datasourceUsername = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, String.class);
		this.datasourcePassword = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, String.class);
		
	}
	
	private String createDatasourceUrl(String server, String port, String database) {
		String url = String.format("jdbc:postgresql://%s:%s/%s", server, port, database);
		return url;
	}

	public File getConfigIniFile() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		Path configPath = Paths.get(s, "config.cfg");
		
		return configPath.toFile();
	}

}
