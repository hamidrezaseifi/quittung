package de.seifi.rechnung_manager_app.models.print;

public class RechnungPrintJRRow implements IPrintJRRow {

    private String produkt;
    private String nummer;
    private String rechnungCreate;
    private String customerName;
    private String address1;
    private String address2;
    private Integer paymentType;
    private String artikelNummer;
    private Integer menge;
    private Float preis;
    private Float gesamt;

    public RechnungPrintJRRow() {
    }

    public RechnungPrintJRRow(String produkt,
                              String nummer,
                              String rechnungCreate,
                              String customerName,
                              String address1,
                              String address2,
                              Integer paymentType,
                              String artikelNummer,
                              Integer menge,
                              Float preis,
                              Float gesamt) {
        this.produkt = produkt;
        this.nummer = nummer;
        this.rechnungCreate = rechnungCreate;
        this.customerName = customerName;
        this.address1 = address1;
        this.address2 = address2;
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

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
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
}
