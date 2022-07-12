package de.seifi.rechnung_manager.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfigUtils {
	
	private static final String CONFIG_KEY_DATABASE_SECTOR = "database";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_DRIVER_CLASS_NAME = "quittung.datasource.driver-class-name";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD = "quittung.datasource.password";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME = "quittung.datasource.username";

	private static final String CONFIG_KEY_QUITTUNG_DATASOURCE_URL = "quittung.datasource.url";

	
	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/rechnung";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_PASSWORD = "7342";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_USERNAME = "postgres";

	private static final String CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_DRIVER_CLASS_NAME = "org.postgresql.Driver";

	private String datasourceUrl;
	
	private String datasourceUsername;
	
	private String datasourcePassword;
	
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
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_URL, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_URL);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_USERNAME);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_PASSWORD);
		ini.put(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DRIVER_CLASS_NAME, CONFIG_DEFAULT_VALUE_QUITTUNG_DATASOURCE_DRIVER_CLASS_NAME);
		
		ini.store();
		
	}
	
	private void readConfig(File configFile) throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(configFile);
		this.datasourceUrl = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_URL, String.class);
		this.datasourceUsername = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_USERNAME, String.class);
		this.datasourcePassword = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_PASSWORD, String.class);
		this.datasourceDriverClassName = ini.get(CONFIG_KEY_DATABASE_SECTOR, CONFIG_KEY_QUITTUNG_DATASOURCE_DRIVER_CLASS_NAME, String.class);
		
	}

	private File getConfigIniFile() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		Path configPath = Paths.get(s, "config.cfg");
		
		return configPath.toFile();
	}

}
