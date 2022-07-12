package de.seifi.rechnung_manager.utils;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfigUtils {
	
	@Value("${quittung.datasource.url}")
	private String datasourceUrl;
	
	@Value("${quittung.datasource.username}")
	private String datasourceUsername;
	
	@Value("${quittung.datasource.password}")
	private String datasourcePassword;
	
	@Value("${quittung.datasource.driver-class-name}")
	private String datasourceDriverClassName;
	
	
	@Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(datasourceDriverClassName);
        dataSource.setJdbcUrl(datasourceUrl);
        dataSource.setUsername(datasourceUsername);
        dataSource.setPassword(datasourcePassword);

        return dataSource;
    }

}
