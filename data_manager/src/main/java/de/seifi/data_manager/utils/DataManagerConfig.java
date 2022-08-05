package de.seifi.data_manager.utils;

import java.util.Arrays;
import java.util.List;

import de.seifi.rechnung_common.config.ConfigItem;
import de.seifi.rechnung_common.config.ConfigReader;
import de.seifi.rechnung_common.config.IConfigItem;

public class DataManagerConfig extends ConfigReader {

	private static final String CONFIG_KEY_GENERAL_SECTOR = "general";

	private static final String CONFIG_KEY_POSTGRES_DUMP_APP_PATH = "postgres_dump_path";

	public static final String CONFIG_FILE_NAME = "data_manager_config.cfg";

	
	private static final List<IConfigItem> configItemList = Arrays.asList(
			new ConfigItem(CONFIG_KEY_GENERAL_SECTOR, CONFIG_KEY_POSTGRES_DUMP_APP_PATH, ""));


	public DataManagerConfig() {
		super(CONFIG_FILE_NAME, configItemList);
		
	}
	
	public String getPostgresDumpAppPath() {
		return getValue(CONFIG_KEY_POSTGRES_DUMP_APP_PATH);
	}
	
	public void setPostgresDumpAppPath(String path) {
		setValue(CONFIG_KEY_POSTGRES_DUMP_APP_PATH, path);
		
	}

}
