package de.seifi.quittung.models;

import java.util.ArrayList;
import java.util.List;

public class QuittungModel {
    private String nummer;
    private String date;
    private String time;
    private List<QuittungItemModel> items;
    
    public QuittungModel() {
    	items = new ArrayList<QuittungItemModel>();
    }

	public QuittungModel(String nummer, String date, String time) {
		this();
		this.nummer = nummer;
		this.date = date;
		this.time = time;
		
	}

	public QuittungModel(String nummer, String date, String time, List<QuittungItemModel> items) {
		this();
		this.nummer = nummer;
		this.date = date;
		this.time = time;
		this.items = items;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<QuittungItemModel> getItems() {
		return items;
	}

	public void setItems(List<QuittungItemModel> items) {
		this.items = items;
	}

    

}
