package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.enums.PaymentType;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.enums.RechnungType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class KostenvoranschlagModel {

    private UUID id;

	private UUID customerId;

	private String nummer;

	private String schluesselnummer;

	private String fahrgestellnummer;

	private String fahrzeugschein;

	private RechnungStatus status;

	private UUID userId;

	private LocalDateTime created;

	private LocalDateTime updated;

	private List<RechnungItemModel> items;

    public KostenvoranschlagModel() {
    	super();
    	this.id = null;
		this.customerId = null;
		this.userId = null;
		this.items = new ArrayList<>();


    }
	
	public boolean isNew() {
		return id == null;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public String getSchluesselnummer() {
		return schluesselnummer;
	}

	public void setSchluesselnummer(String schluesselnummer) {
		this.schluesselnummer = schluesselnummer;
	}

	public String getFahrgestellnummer() {
		return fahrgestellnummer;
	}

	public void setFahrgestellnummer(String fahrgestellnummer) {
		this.fahrgestellnummer = fahrgestellnummer;
	}

	public String getFahrzeugschein() {
		return fahrzeugschein;
	}

	public void setFahrzeugschein(String fahrzeugschein) {
		this.fahrzeugschein = fahrzeugschein;
	}

	public RechnungStatus getStatus() {
		return status;
	}

	public void setStatus(RechnungStatus status) {
		this.status = status;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public List<RechnungItemModel> getItems() {
		return items;
	}

	public void setItems(List<RechnungItemModel> items) {
		this.items = items;
	}
}
