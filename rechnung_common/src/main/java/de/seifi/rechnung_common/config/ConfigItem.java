package de.seifi.rechnung_common.config;

public class ConfigItem implements IConfigItem {
	
	private String sector;

	private String name;

	private String value;

	private String defaultValue;

	public ConfigItem() {
		
	}

	public ConfigItem(String sector, String name, String defaultValue) {
		super();
		this.sector = sector;
		this.name = name;
		this.value = "";
		this.defaultValue = defaultValue;
	}

	public ConfigItem(String sector, String name, String value, String defaultValue) {
		super();
		this.sector = sector;
		this.name = name;
		this.value = value;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	

	
}
