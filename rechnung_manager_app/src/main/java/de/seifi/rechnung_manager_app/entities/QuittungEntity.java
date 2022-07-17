package de.seifi.rechnung_manager_app.entities;

import de.seifi.rechnung_manager_app.models.QuittungModel;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "quittung")
public class QuittungEntity extends EntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	private Integer nummer;

	@Column(name="quittung_create")
	private String quittungCreate;

	@Column(name="lifer_date")
	private String liferDate;

	private Timestamp created;

	private Timestamp updated;

	@OneToMany(mappedBy = "quittung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<QuittungItemEntity> items;

    public QuittungEntity() {
    	super();
    	items = new HashSet<>();
    }


	public QuittungEntity(Integer nummer, String quittungCreate, String liferDate) {
		this();
		this.nummer = nummer;
		this.quittungCreate = quittungCreate;
		this.liferDate = liferDate;
	}


	public QuittungEntity(Integer id,
                          Integer nummer,
                          String quittungCreate,
                          String liferDate,
                          Timestamp created,
                          Timestamp updated) {
		this();
		this.id = id;
		this.nummer = nummer;
		this.quittungCreate = quittungCreate;
		this.liferDate = liferDate;
        this.created = created;
        this.updated = updated;
	}


	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNummer() {
		return nummer;
	}

	public void setNummer(Integer nummer) {
		this.nummer = nummer;
	}

	public String getQuittungCreate() {
		return quittungCreate;
	}

	public void setQuittungCreate(String quittungCreate) {
		this.quittungCreate = quittungCreate;
	}

	public String getLiferDate() {
		return liferDate;
	}


	public void setLiferDate(String liferDate) {
		this.liferDate = liferDate;
	}


	public Set<QuittungItemEntity> getItems() {
		return items;
	}

	public void setItems(List<QuittungItemEntity> items) {
		this.items.clear();
		if(items != null) {
			items.forEach(i -> i.setQuittung(this));
			this.items.addAll(items);
		}
		
	}

	public void addItem(QuittungItemEntity item) {
		item.setQuittung(this);
		this.items.add(item);
		
	}

	@Override
	public Timestamp getCreated() {
		return created;
	}

	@Override
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	@Override
	public Timestamp getUpdated() {
		return updated;
	}

	@Override
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}


	public QuittungModel toModel() {
		QuittungModel model = new QuittungModel(id, nummer, quittungCreate, liferDate, created.toLocalDateTime(), updated.toLocalDateTime());
		for(QuittungItemEntity item: items) {
			model.getItems().add(item.toModel());
		}
		
		return model;
	}

    

}
