package de.seifi.rechnung_common.models;

public class TableModel {
	
	private String name;
	
	private String title;

	public TableModel() {
		
	}

	public TableModel(String name, String title) {
		super();
		this.name = name;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	

}
