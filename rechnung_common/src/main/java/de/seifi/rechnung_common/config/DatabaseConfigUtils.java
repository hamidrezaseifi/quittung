package de.seifi.rechnung_common.config;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

import de.seifi.rechnung_common.models.TableModel;

@Configuration
public class DatabaseConfigUtils extends ConfigReader {
	
	private static final String CONFIG_KEY_DATABASE_SECTOR = "database";


	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER = "server";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_PORT = "port";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE = "database";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD = "password";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME = "username";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_IS_H2 = "is_h2";


	private static final String CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_SERVER = "localhost";

	private static final String CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_PORT = "5432";

	private static final String CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_DATABASE = "rechnung";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_PASSWORD = "7342";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_USERNAME = "postgres";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_IS_H2 = "false";

	private static final String BACKUP_HISTORY_TABLE_NAME = "backup_history";


	private static final List<IConfigItem> configItemList = Arrays.asList(
			new ConfigItem(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_SERVER),
			new ConfigItem(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_PORT),
			new ConfigItem(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_DATABASE),
			new ConfigItem(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_PASSWORD),
			new ConfigItem(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_USERNAME),
			new ConfigItem(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_IS_H2, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_IS_H2));
	

	private static final List<TableModel> existingTables = 
			Arrays.asList(new TableModel("produkt", "Produkte"), 
					new TableModel("rechnung", "Rechnungen"), 
					new TableModel("rechnung_item", "Rechnung-Produkte"), 
					new TableModel("customer", "Kunden"));
	
	@Value("${quittung.datasource.driver-class-name}")
	private String datasourceDriverClassName;

	@Value("${spring.profiles.active}")
	private String activeProfile;
	
	public DatabaseConfigUtils() {
		super("db_config.cfg", configItemList);
	}
	
	public DatabaseConfigUtils(String datasourceDriverClassName) {
		super("db_config.cfg", configItemList);
		
		this.datasourceDriverClassName = datasourceDriverClassName;
	}

	@Bean
    public DataSource dataSource() {
		readConfig();
		
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(datasourceDriverClassName);
        dataSource.setJdbcUrl(getDatasourceUrl());
        dataSource.setUsername(getDatasourceUsername());
        dataSource.setPassword(getDatasourcePassword());
        

        return dataSource;
    }

	public boolean testConnection(String driver, String server, String port, String database, String username, String password) throws SQLException, RuntimeException {
		
		String dbUrl = getDatasourceUrl(server, port, database);
		
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
        DatabaseMetaData mData = dataSource.getConnection().getMetaData();
        ResultSet rs = mData.getTables(null, null, "%", null);
        
        List<String> checkingTables = new ArrayList<String>();
        checkingTables.addAll(existingTables.stream().map(t -> t.getName()).collect(Collectors.toList()));
        while (rs.next()) {
        	if(rs.getString("TABLE_TYPE")!= null && rs.getString("TABLE_TYPE").toLowerCase().equals("table")) {
        		String tableName = rs.getString("TABLE_NAME");
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

		setValue(CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, server);
		setValue(CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, port);
		setValue(CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, database);
		setValue(CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, username);
		setValue(CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, password);

		
		return saveConfig();
    }
	
	private String getDatasourceUrl(String server, String port, String database) {
		String url = String.format("jdbc:postgresql://%s:%s/%s", server, port, database);
		return url;
	}
	
	private String getDatasourceUrl() {
		if(isH2Database()){
			return "jdbc:h2:mem:testdb";

		}
		return getDatasourceUrl(getServer(), getPort(), getDatabase());
	}

	private boolean isH2Database() {
		return datasourceDriverClassName.equals("org.h2.Driver");
	}

	public List<TableModel> getTableModelList(){
		return existingTables;
	}
	
	public String getTableInfo(TableModel model) throws SQLException {
		
		String sql = String.format("select count(*) AS COUNT from %s", model.getName());
		
        Connection conn = null;
        int rowCount = 0;
		try {
			DataSource dataSource = getCurrentDataSource();
			conn = dataSource.getConnection();
	        
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()) {
	            rowCount = rs.getInt("COUNT");
	        }

		} catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.close();
				}
			}
			catch(Exception e) {
				
			}
			
		}
        
        return String.format("Daten-Zahl: %d", rowCount);
        
	}
	
	public boolean clearTable(TableModel model) throws SQLException {
		String sql = String.format("delete from %s", model.getName());
		
        Connection conn = null;
		try {
			DataSource dataSource = getCurrentDataSource();
			conn = dataSource.getConnection();
	        
	        Statement statement = conn.createStatement();
			statement.executeUpdate(sql);

		} catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.close();
				}
			}
			catch(Exception e) {
				
			}
			
		}
		
		return true;
	}
	
	private DataSource getCurrentDataSource() {
		HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(this.datasourceDriverClassName);
        dataSource.setJdbcUrl(this.getDatasourceUrl());
        dataSource.setUsername(this.getDatasourceUsername());
        dataSource.setPassword(this.getDatasourcePassword());
        
        return dataSource;
	}


	public String getDatasourcePassword() {
		if(isH2Database()){
			return "";
		}

		return getValue(CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD);
	}

	public String getDatasourceUsername() {
		if(isH2Database()){
			return "sa";
		}
		return getValue(CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME);	
	}

	public String getDatabase() {

		return getValue(CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE);
	}

	public String getPort() {
		
		return getValue(CONFIG_KEY_QUITTUNG_DATASOURCE_PORT);
	}

	public String getServer() {
		
		return getValue(CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER);
	}

    public List<String> getBackupInfo() {

		String sql = String.format("select max(backup_ts) as ts, id, table_name, backup_result, updated  from backup_history " +
								   "group by id, table_name, backup_result, updated order by table_name",
								   BACKUP_HISTORY_TABLE_NAME);
		List<String> results = new ArrayList<>();
		Connection conn = null;
		try {
			DataSource dataSource = getCurrentDataSource();
			conn = dataSource.getConnection();

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {

				Date ts = rs.getDate("ts");
				String id = rs.getString("id");
				String table_name = rs.getString("table_name");
				String backup_result = rs.getString("backup_result");
				Date updated = rs.getDate("updated");

				results.add(String.format("%s: letzte Backup(%s)", table_name, ts));

			}

		} catch (SQLException e) {
			results.add("Fehler beim Extrahieren von den Tabellen Info: " + e.getLocalizedMessage());
		}
		finally {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.close();
				}
			}
			catch(Exception e) {

			}

		}

		if(results.isEmpty()){
			results.add("Keine Backup Historie!");
		}
		return results;

	}
}
