package de.seifi.rechnung_manager_app.models.print;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import net.sf.jasperreports.engine.JRField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RechnungPrintJRDataSource extends PrintJRDataSourceBase {

    private final List<RechnungPrintJRRow> rows;

    private final String createDate;
    private final float anzahlung;
    private final float restZahlung;

    public RechnungPrintJRDataSource(RechnungModel rechnungModel, CustomerModel customerModel) {
        this.rows = rechnungModel.getItems().stream().map(item -> createRechnungPrintJRRow(rechnungModel, customerModel, item)).collect(
                Collectors.toList());

        this.createDate = rechnungModel.getRechnungCreate();
        this.anzahlung = rechnungModel.getAnzahlung();
        this.restZahlung = rechnungModel.getRestZahlung();
    }

    private RechnungPrintJRRow createRechnungPrintJRRow(RechnungModel rechnungModel,
                                                        CustomerModel customerModel,
                                                        RechnungItemModel itemModel) {
        RechnungPrintJRRow
                printJRRow = new RechnungPrintJRRow(itemModel.getProdukt(),
                                             rechnungModel.getNummer().toString(),
                                             rechnungModel.getRechnungCreate(),
                                             customerModel.getCustomerName(),
                                             customerModel.getPrintLine1(),
                                             customerModel.getPrintLine2(),
                                             rechnungModel.getPaymentType().getValue(),
                                             itemModel.getArtikelNummer(),
                                             itemModel.getMenge(),
                                             itemModel.getPreis(),
                                             itemModel.getGesmt());
        return printJRRow;
    }

    @Override
    public Object getFieldValue(JRField jrField) {
        RechnungPrintJRRow row = rows.get(this.rowIndex);

        return row.getFieldValue(jrField.getName());
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
