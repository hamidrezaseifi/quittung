package de.seifi.rechnung_manager_app.models.print;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class QuittungPrintJRDataSource extends PrintJRDataSourceBase {

    private final List<QuittungPrintJRRow> rows;

    private final String createDate;
    private final float anzahlung;
    private final float restZahlung;

    public QuittungPrintJRDataSource(List<QuittungPrintJRRow> rows,
                                     String createDate) {
        this.rows = rows;
        this.createDate = createDate;
        this.anzahlung = 0;
        this.restZahlung = this.getTotalNeto();
    }

    public QuittungPrintJRDataSource(RechnungModel rechnungModel) {
        this.rows = rechnungModel.getItems().stream().map(item -> extractQuittungPrintJRRow(rechnungModel, item)).collect(
                Collectors.toList());

        this.createDate = rechnungModel.getRechnungCreate();
        this.anzahlung = rechnungModel.getAnzahlung();
        this.restZahlung = rechnungModel.getRestZahlung();
    }

    private QuittungPrintJRRow extractQuittungPrintJRRow(RechnungModel rechnungModel,
                                                         RechnungItemModel itemModel) {
        QuittungPrintJRRow
                row = new QuittungPrintJRRow(itemModel.getProdukt(),
                                             rechnungModel.getNummer().toString(),
                                             rechnungModel.getRechnungCreate(),
                                             rechnungModel.getPaymentType().getValue(),
                                             itemModel.getArtikelNummer(),
                                             itemModel.getMenge(),
                                             itemModel.getPreis(),
                                             itemModel.getGesmt());
        return row;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        QuittungPrintJRRow row = rows.get(this.rowIndex);

        switch (jrField.getName()){
            case "produkt": return row.getProdukt();
            case "nummer": return row.getNummer();
            case "rechnung_reate": return row.getRechnungCreate();
            case "payment_type": return row.getPaymentType();
            case "artikel_nummer": return row.getArtikelNummer();
            case "menge": return row.getMenge();
            case "preis": return row.getPreis();
            case "gesamt": return row.getGesamt();

        }
        return null;
    }

    @Override
    protected int getRowCount() {
        return rows.size();
    }

    @Override
    protected Float getTotalNeto() {
        AtomicReference<Float> totalNeto = new AtomicReference<>(0f);

        this.rows.forEach(r -> totalNeto.updateAndGet(v -> v + r.getGesamt()));

        return totalNeto.get();
    }

    @Override
    protected String getCreateDate() {
        return this.createDate;
    }

    @Override
    protected Map<String, Object> preparePrintParameterMap() {
        Map<String, Object> printParameterMap = new HashMap<>();
        Float totalNeto = this.getTotalNeto();
        printParameterMap.put(IPrintJRDataSource.RECHNUNG_CREATE_DATE, this.getCreateDate());
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_ROWS, this.getRowCount());
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_NETO, totalNeto);
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_MWT, totalNeto * 19 / 100);
        printParameterMap.put(IPrintJRDataSource.PARAMETER_LOGO_PATH, RechnungManagerFxApp.getLocalJasperPrintLogoPath());

        printParameterMap.put(IPrintJRDataSource.PARAMETER_ANZAHLUNG, this.anzahlung);
        printParameterMap.put(IPrintJRDataSource.PARAMETER_REST_ZAHLUNG, this.restZahlung);

        return printParameterMap;
    }
}
