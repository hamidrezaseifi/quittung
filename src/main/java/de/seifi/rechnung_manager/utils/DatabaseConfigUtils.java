package de.seifi.rechnung_manager.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	

	private String datasourceUrl;
	
	private String datasourceUsername;
	
	private String datasourcePassword;
	
	@Value("${quittung.datasource.driver-class-name}")
	private String datasourceDriverClassName;
	
	
	@Bean
    public DataSource dataSource() {
		File configFile = getConfigIniFile();
		
		try {
			if(!configFile.exists()) {
				initializeConfig(configFile);
			}
			readConfig(configFile);

		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(datasourceDriverClassName);
        dataSource.setJdbcUrl(datasourceUrl);
        dataSource.setUsername(datasourceUsername);
        dataSource.setPassword(datasourcePassword);
        

        return dataSource;
    }
	
	private void initializeConfig(File configFile) throws InvalidFileFormatException, IOException {
		configFile.createNewFile();
		
		Ini ini = new Ini(configFile);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_SERVER);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_PORT);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_DATABASE);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_USERNAME);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_PASSWORD);
		
		ini.store();
		
	}
	
	private void readConfig(File configFile) throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(configFile);
		String server = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, String.class);
		String port = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, String.class);
		String database = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, String.class);
		this.datasourceUrl = String.format("jdbc:postgresql://%s:%s/%s", server, port, database);
		
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_SERVER, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_SERVER);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PORT, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_PORT);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DATABASE, CONFIG_DEFAULT_VALU_QUITTUNG_DATASOURCE_DATABASE);

		
		this.datasourceUsername = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, String.class);
		this.datasourcePassword = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, String.class);
		
	}

	private File getConfigIniFile() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		Path configPath = Paths.get(s, "config.cfg");
		
		return configPath.toFile();
	}

}
