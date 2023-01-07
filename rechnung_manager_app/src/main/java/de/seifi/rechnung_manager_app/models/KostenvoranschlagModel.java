package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
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

	private String schluesselNummer;

	private String fahrgestellNummer;

	private String fahrzeugSchein;

	private KostenvoranschlagStatus status;

	private LocalDateTime created;

	private LocalDateTime updated;

	private List<KostenvoranschlagItemModel> items;

    public KostenvoranschlagModel() {
    	super();
    	this.id = null;
		this.customerId = null;
		this.items = new ArrayList<>();
    }

	public KostenvoranschlagModel(UUID id,
								  UUID customerId,
								  String nummer,
								  String schluesselNummer,
								  String fahrgestellNummer,
								  String fahrzeugSchein,
								  KostenvoranschlagStatus status,
								  LocalDateTime created,
								  LocalDateTime updated) {
		this.id = id;
		this.customerId = customerId;
		this.nummer = nummer;
		this.schluesselNummer = schluesselNummer;
		this.fahrgestellNummer = fahrgestellNummer;
		this.fahrzeugSchein = fahrzeugSchein;
		this.status = status;
		this.created = created;
		this.updated = updated;
	}

	public KostenvoranschlagModel(String nummer,
								  String schluesselNummer,
								  String fahrgestellNummer,
								  String fahrzeugSchein,
								  KostenvoranschlagStatus status) {
		this.id = id;
		this.customerId = customerId;
		this.nummer = nummer;
		this.schluesselNummer = schluesselNummer;
		this.fahrgestellNummer = fahrgestellNummer;
		this.fahrzeugSchein = fahrzeugSchein;
		this.status = status;
		this.created = created;
		this.updated = updated;
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

	public String getSchluesselNummer() {
		return schluesselNummer;
	}

	public void setSchluesselNummer(String schluesselNummer) {
		this.schluesselNummer = schluesselNummer;
	}

	public String getFahrgestellNummer() {
		return fahrgestellNummer;
	}

	public void setFahrgestellNummer(String fahrgestellNummer) {
		this.fahrgestellNummer = fahrgestellNummer;
	}

	public String getFahrzeugSchein() {
		return fahrzeugSchein;
	}

	public void setFahrzeugSchein(String fahrzeugSchein) {
		this.fahrzeugSchein = fahrzeugSchein;
	}

	public KostenvoranschlagStatus getStatus() {
		return status;
	}

	public void setStatus(KostenvoranschlagStatus status) {
		this.status = status;
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

	public List<KostenvoranschlagItemModel> getItems() {
		return items;
	}

	public void setItems(List<KostenvoranschlagItemModel> items) {
		this.items = items;
	}
}