package de.seifi.rechnung_manager_app.models.print;

public class QuittungPrintJRRow implements IPrintJRRow {

    private String produkt;
    private String nummer;
    private String rechnungCreate;
    private Integer paymentType;
    private String artikelNummer;
    private Integer menge;
    private Float preis;
    private Float gesamt;

    public QuittungPrintJRRow() {
    }

    public QuittungPrintJRRow(String produkt,
                              String nummer,
                              String rechnungCreate,
                              Integer paymentType,
                              String artikelNummer,
                              Integer menge,
                              Float preis,
                              Float gesamt) {
        this.produkt = produkt;
        this.nummer = nummer;
        this.rechnungCreate = rechnungCreate;
        this.paymentType = paymentType;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
        this.gesamt = gesamt;
    }

    public String getProdukt() {
        return produkt;
    }

    public String getNummer() {
        return nummer;
    }

    public String getRechnungCreate() {
        return rechnungCreate;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public String getArtikelNummer() {
        return artikelNummer;
    }

    public Integer getMenge() {
        return menge;
    }

    public Float getPreis() {
        return preis;
    }

    @Override
    public Float getGesamt() {
        return gesamt;
    }

    @Override
    public Object getFieldValue(String field) {
        switch (field){
            case "produkt": return getProdukt();
            case "nummer": return getNummer();
            case "rechnung_create": return getRechnungCreate();
            case "customer_name": return "";
            case "address1": return "";
            case "address2": return "";
            case "payment_type": return getPaymentType();
            case "artikel_nummer": return getArtikelNummer();
            case "menge": return getMenge();
            case "preis": return getPreis();
            case "gesamt": return getGesamt();

        }
        return null;
    }
}
