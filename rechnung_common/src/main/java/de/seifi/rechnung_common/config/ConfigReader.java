package de.seifi.rechnung_common.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class ConfigReader {

	private final Map<String, IConfigItem> configItems;
	
	private final String configFileName;
	
	public ConfigReader(String configFileName, List<IConfigItem> configItemList) {
		
		this.configItems = configItemList.stream().collect(Collectors.toMap(c -> c.getName(), c -> c));
		this.configFileName = configFileName;
		
		readConfig();
	}

	public boolean readConfig() {
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
	
	private void readConfig(File configFile) throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(configFile);
		
		for(IConfigItem configItem : configItems.values()) {
			
			String value = ini.get(configItem.getSector(), configItem.getName(), String.class);
			
			configItem.setValue(value);
		}
		
	}
	
    public boolean saveConfig() throws IOException {
    	File configFile = getConfigIniFile();
    	
    	return saveConfig(configFile);
    }
	
    public boolean saveConfig(File configFile) throws IOException {
    	if(configFile.exists() == false) {
    		configFile.createNewFile();
    	}
    	
		Ini ini = new Ini(configFile);
		
		for(IConfigItem configItem : configItems.values()) {
			
			ini.put(configItem.getSector(), configItem.getName(), configItem.getValue());
			
		}
		
		ini.store();
		
		return true;
    }
	
    public boolean initializeConfig(File configFile) throws IOException {
    	if(configFile.exists() == false) {
    		configFile.createNewFile();
    	}
    	
		Ini ini = new Ini(configFile);
		
		for(IConfigItem configItem : configItems.values()) {
			
			ini.put(configItem.getSector(), configItem.getName(), configItem.getDefaultValue());
			
		}
		
		ini.store();
		
		return true;
    }
    
	public File getConfigIniFile() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		Path configPath = Paths.get(s, configFileName);
		
		return configPath.toFile();
	}

	public String getValue(String name) {
		return configItems.get(name).getValue();
	}

	public void setValue(String name, String value) {
		configItems.get(name).setValue(value);
	}

	public Map<String, String> getValues() {
		return configItems.values().stream().collect(Collectors.toMap(c -> c.getName(), c -> c.getValue()));
	}
	
	
}
