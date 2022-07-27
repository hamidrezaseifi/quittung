package de.seifi.db_connection.utils;

import java.util.Arrays;
import java.util.List;

import de.seifi.rechnung_common.config.ConfigItem;
import de.seifi.rechnung_common.config.ConfigReader;
import de.seifi.rechnung_common.config.IConfigItem;

public class AppConfig extends ConfigReader {
	
	private static final String CONFIG_KEY_GENERAL_SECTOR = "general";

	private static final String CONFIG_KEY_POSTGRES_DUMP_APP_PATH = "postgres_dump_path";


	
	private static final List<IConfigItem> configItemList = Arrays.asList(
			new ConfigItem(CONFIG_KEY_GENERAL_SECTOR, CONFIG_KEY_POSTGRES_DUMP_APP_PATH, ""));

	
	public AppConfig() {
		super("app_config.cfg", configItemList);
		
	}
	
	public String getPostgresDupmAppPath() {
		return getValue(CONFIG_KEY_POSTGRES_DUMP_APP_PATH);
	}
	
	public void setPostgresDupmAppPath(String path) {
		setValue(CONFIG_KEY_POSTGRES_DUMP_APP_PATH, path);
		
	}

}
