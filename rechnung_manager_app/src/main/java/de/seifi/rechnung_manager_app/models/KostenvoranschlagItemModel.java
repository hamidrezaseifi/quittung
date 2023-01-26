package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.ui.TableUtils;

import java.time.LocalDateTime;
import java.util.UUID;


public class KostenvoranschlagItemModel implements IReportLabelModel {

	private UUID id;

	private String produkt;

    private String originalNummer;

    private String teilNummer;

	private String marke;

	private float preis;

    private boolean bestellt;

	private LocalDateTime created;

	private LocalDateTime updated;

    public KostenvoranschlagItemModel() {
    	super();
    	id = null;
    }

    public KostenvoranschlagItemModel(UUID id,
                                      String produkt,
                                      String originalNummer,
                                      String teilNummer,
                                      String marke,
                                      boolean bestellt,
                                      float preis,
                                      LocalDateTime created,
                                      LocalDateTime updated) {
        this.id = id;
        this.produkt = produkt;
        this.originalNummer = originalNummer;
        this.teilNummer = teilNummer;
        this.marke = marke;
        this.preis = preis;
        this.bestellt = bestellt;
        this.created = created;
        this.updated = updated;
    }

    public KostenvoranschlagItemModel(String produkt,
                                      String originalNummer,
                                      String teilNummer,
                                      String marke,
                                      boolean bestellt,
                                      float preis) {
        this.id = id;
        this.produkt = produkt;
        this.originalNummer = originalNummer;
        this.teilNummer = teilNummer;
        this.marke = marke;
        this.preis = preis;
        this.bestellt = bestellt;
        this.created = created;
        this.updated = updated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProdukt() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        this.produkt = produkt;
    }

    public String getOriginalNummer() {
        return originalNummer;
    }

    public void setOriginalNummer(String originalNummer) {
        this.originalNummer = originalNummer;
    }

    public String getTeilNummer() {
        return teilNummer;
    }

    public void setTeilNummer(String teilNummer) {
        this.teilNummer = teilNummer;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public float getPreis() {
        return preis;
    }

    public void setPreis(float preis) {
        this.preis = preis;
    }

    public boolean isBestellt() {
        return bestellt;
    }

    public void setBestellt(boolean bestellt) {
        this.bestellt = bestellt;
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

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String getReportLabel() {
        String besteltLbl = bestellt ? "bestellt" : "nit bestellt";

        return String.format("%s (%s, %s): %s --> (%s)",
                             produkt,
                             originalNummer,
                             teilNummer,
                             TableUtils.formatGeld(preis),
                             besteltLbl);
    }
}
