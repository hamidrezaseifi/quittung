package de.seifi.rechnung_manager_app.models.print;

import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import net.sf.jasperreports.engine.JRField;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RechnungPrintJRDataSource extends PrintJRDataSourceBase {

    private final List<RechnungPrintJRRow> rows;

    private final String createDate;

    public RechnungPrintJRDataSource(RechnungModel rechnungModel, CustomerModel customerModel) {
        this.rows = rechnungModel.getItems().stream().map(item -> createRechnungPrintJRRow(rechnungModel, customerModel, item)).collect(
                Collectors.toList());

        this.createDate = rechnungModel.getRechnungCreate();
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

        switch (jrField.getName()){
            case "produkt": return row.getProdukt();
            case "nummer": return row.getNummer();
            case "rechnung_create": return row.getRechnungCreate();
            case "customer_name": return row.getCustomerName();
            case "address1": return row.getAddress1();
            case "address2": return row.getAddress2();
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
}
