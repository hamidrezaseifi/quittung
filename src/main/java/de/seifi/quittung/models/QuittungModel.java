package de.seifi.quittung.models;

import java.util.ArrayList;
import java.util.List;

public class QuittungModel {
    private Integer id;
    private Integer nummer;
    private String date;
    private String time;
    private List<QuittungItemModel> items;
    
    public QuittungModel() {
		this.id = 0;
		this.nummer = 0;
    	items = new ArrayList<QuittungItemModel>();
    }

	public QuittungModel(Integer id, Integer nummer, String date, String time) {
		this();
		this.id = id;
		this.nummer = nummer;
		this.date = date;
		this.time = time;
		
	}

	public QuittungModel(Integer id, Integer nummer, String date, String time, List<QuittungItemModel> items) {
		this();
		this.id = id;
		this.nummer = nummer;
		this.date = date;
		this.time = time;
		this.items = items;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNummer() {
		return nummer;
	}

	public void setNummer(Integer nummer) {
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
