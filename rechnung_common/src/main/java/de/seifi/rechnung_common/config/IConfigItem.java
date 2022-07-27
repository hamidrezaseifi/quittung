package de.seifi.rechnung_common.config;

public interface IConfigItem {
	
	String getSector();
	
	String getName();
	
	String getValue();
	
	void setValue(String value);
	
	String getDefaultValue();
	

}
