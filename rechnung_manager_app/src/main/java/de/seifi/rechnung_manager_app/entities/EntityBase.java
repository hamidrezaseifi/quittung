package de.seifi.rechnung_manager_app.entities;

import java.time.LocalDateTime;


public abstract class EntityBase {
	
    
    public EntityBase() {
		this.setId(null);
		this.setCreated(LocalDateTime.now());
		this.setUpdated(LocalDateTime.now());
    }

	public boolean isNew() {
		return getId() == null;
	}

	public abstract void setId(Integer id);

	public abstract Integer getId();


	public abstract LocalDateTime getCreated();


	public abstract  void setCreated(LocalDateTime created);


	public abstract LocalDateTime getUpdated();


	public abstract  void setUpdated(LocalDateTime updated);
	   
	
}
