package de.seifi.rechnung_manager_app.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.seifi.rechnung_manager_app.enums.PaymentType;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.enums.RechnungType;

public class RechnungModel {

	public static final UUID QUITTUNG_CUSTOMER_ID = null;

    private UUID id;

	private UUID customerId;
	
	private UUID referenceId;

	private Integer nummer;
	   
	private String rechnungCreate;
	   
	private String liferDate;

	private RechnungType rechnungType;

	private Integer rechnungVersion;

	private PaymentType paymentType;

	private RechnungStatus status;

	private UUID userId;

	private LocalDateTime created;
	
	private LocalDateTime updated;

	private List<RechnungItemModel> items;

    public RechnungModel() {
    	super();
    	this.id = null;
		this.customerId = null;
		this.userId = null;
		this.items = new ArrayList<>();
		this.rechnungVersion = 1;
		this.referenceId = null;

    }

	public RechnungModel(UUID referenceId,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 int rechnungVersion,
						 PaymentType paymentType,
						 RechnungType rechnungType,
						 RechnungStatus status,
						 UUID userId) {
		this();
		this.referenceId = referenceId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.rechnungVersion = rechnungVersion;
		this.paymentType = paymentType;
		this.rechnungType = rechnungType;
		this.status = status;
		this.userId = userId;
	}


	public RechnungModel(UUID id,
						 UUID customerId,
						 UUID referenceId,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 int rechnungVersion,
						 int paymentType,
						 int rechnungType,
						 int status,
						 UUID userId,
						 LocalDateTime created,
						 LocalDateTime updated) {
		this();
		this.id = id;
		this.customerId = customerId;
		this.referenceId = referenceId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.rechnungVersion = rechnungVersion;
		this.rechnungType = RechnungType.ofValue(rechnungType);
		this.paymentType = PaymentType.ofValue(paymentType);
		this.status = RechnungStatus.ofValue(status);
        this.created = created;
        this.updated = updated;
		this.userId = userId;
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

	public UUID getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(UUID referenceId) {
		this.referenceId = referenceId;
	}

	public boolean hasReference() {
		return referenceId != null;
	}

	public Integer getNummer() {
		return nummer;
	}

	public void setNummer(Integer nummer) {
		this.nummer = nummer;
	}


	public String getRechnungCreate() {
		return rechnungCreate;
	}


	public void setRechnungCreate(String rechnungCreate) {
		this.rechnungCreate = rechnungCreate;
	}


	public String getLiferDate() {
		return liferDate;
	}


	public void setLiferDate(String liferDate) {
		this.liferDate = liferDate;
	}


	public List<RechnungItemModel> getItems() {
		return items;
	}

	public void setItems(List<RechnungItemModel> items) {
		this.items = items;
	}

	public RechnungType getRechnungType() {
		return rechnungType;
	}

	public void setRechnungType(RechnungType rechnungType) {
		this.rechnungType = rechnungType;
	}

	public Integer getRechnungVersion() {
		return rechnungVersion;
	}

	public void setRechnungVersion(Integer rechnungVersion) {
		this.rechnungVersion = rechnungVersion;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = PaymentType.ofValue(paymentType);
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

	public float getGesamt(){
		float gesamt = 0.0f;

		for(RechnungItemModel item: items) {
			gesamt += item.getGesmt();
		}

		return gesamt;
	}

	public void changeToNew(){
		setId(null);
		setUpdated(null);
		setCreated(null);
	}

	public void setExemplarOf(RechnungModel reference){
		changeToNew();
		setRechnungVersion(reference.getRechnungVersion() + 1);

		UUID referenceId = reference.getMainReferenceId();

		setReferenceId(referenceId);
	}

	public UUID getMainReferenceId() {
		UUID referenceId = getReferenceId();
		if(referenceId == null){
			referenceId = getId();
		}
		return referenceId;
	}

	public RechnungModel clone(){
		RechnungModel model = new RechnungModel(this.getId(),
				this.getCustomerId(),
				this.getReferenceId(),
				this.getNummer(),
				this.getRechnungCreate(),
				this.getLiferDate(),
				this.getRechnungVersion(),
				this.getPaymentType().getValue(),
				this.getRechnungType().getValue(),
				this.getStatus().getValue(),
				this.getUserId(),
				this.getCreated(),
				this.getUpdated());

		model.setItems(this.getItems().stream().map(RechnungItemModel::clone).collect(Collectors.toList()));

		return model;


	}
    

}
