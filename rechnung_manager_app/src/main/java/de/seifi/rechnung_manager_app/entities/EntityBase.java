package de.seifi.rechnung_manager_app.entities;

import java.sql.Timestamp;
import java.util.UUID;


public abstract class EntityBase {
	
    
    public EntityBase() {
		this.setId(null);
		this.setCreated(new Timestamp(System.currentTimeMillis()));
		this.setUpdated(new Timestamp(System.currentTimeMillis()));
    }

	public abstract void setId(Integer id);


	public abstract Timestamp getCreated();


	public abstract  void setCreated(Timestamp created);


	public abstract Timestamp getUpdated();


	public abstract  void setUpdated(Timestamp updated);

    

}
