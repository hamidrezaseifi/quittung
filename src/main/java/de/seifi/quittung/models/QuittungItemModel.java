package de.seifi.quittung.models;

public class QuittungItemModel {
    private String bezeichnung;
    private int menge;
    private float preis;

    public QuittungItemModel() {
    }

    public QuittungItemModel(String bezeichnung,
                             int menge,
                             float preis) {
        this.bezeichnung = bezeichnung;
        this.menge = menge;
        this.preis = preis;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public float getPreis() {
        return preis;
    }

    public void setPreis(float preis) {
        this.preis = preis;
    }

    public float getGesmt() {
        return preis * menge;
    }


}
