package de.seifi.rechnung_manager_app.models.print;

import de.seifi.rechnung_manager_app.models.KostenvoranschlagItemModel;

public class KostenvoranschlagPrintJRRow implements IPrintJRRow {

    private String produkt;
    private String originalNummer;
    private String teilNummer;
    private String marke;
    private String bestellt;
    private Float preis;

    public KostenvoranschlagPrintJRRow() {
    }

    public KostenvoranschlagPrintJRRow(KostenvoranschlagItemModel itemModel) {
        this.produkt = itemModel.getProdukt();
        this.originalNummer = itemModel.getOriginalNummer();
        this.teilNummer = itemModel.getTeilNummer();
        this.marke = itemModel.getMarke();
        this.bestellt = itemModel.isBestellt() ? "X" : "";
        this.preis = itemModel.getPreis();
    }

    @Override
    public Float getGesamt() {
        return null;
    }

    @Override
    public Object getFieldValue(String field) {
        switch (field){
            case "produkt": return produkt;
            case "originalNummer": return originalNummer;
            case "teilNummer": return teilNummer;
            case "marke": return marke;
            case "preis": return preis;
            case "bestellt": return bestellt;

        }
        return null;
    }

    public Float getPreis() {
        return preis;
    }
}
