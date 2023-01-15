package de.seifi.rechnung_manager_app.models.print;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import net.sf.jasperreports.engine.JRField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class KostenvoranschlagPrintJRDataSource extends PrintJRDataSourceBase {

    private final List<KostenvoranschlagPrintJRRow> rows;

    private final String createDate;

    private final String customerName;


    public KostenvoranschlagPrintJRDataSource(KostenvoranschlagModel kostenvoranschlagModel, CustomerModel customerModel) {
        this.rows = kostenvoranschlagModel.getItems().stream().map(item -> createRechnungPrintJRRow(item)).collect(
                Collectors.toList());

        this.createDate = GeneralUtils.formatDate(kostenvoranschlagModel.getCreated().toLocalDate());
        this.customerName = customerModel.getCustomerName();
    }

    private KostenvoranschlagPrintJRRow createRechnungPrintJRRow(KostenvoranschlagItemModel itemModel) {
        KostenvoranschlagPrintJRRow
                printJRRow = new KostenvoranschlagPrintJRRow(itemModel);
        return printJRRow;
    }

    @Override
    public Object getFieldValue(JRField jrField) {

        KostenvoranschlagPrintJRRow row = rows.get(this.rowIndex);

        return row.getFieldValue(jrField.getName());
    }

    @Override
    protected int getRowCount() {
        return rows.size();
    }

    @Override
    protected Float getTotalNeto() {
        AtomicReference<Float> totalNeto = new AtomicReference<>(0f);

        this.rows.forEach(r -> totalNeto.updateAndGet(v -> v + r.getPreis()));

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

        printParameterMap.put("createDate", this.getCreateDate());
        printParameterMap.put("totalPrise", totalNeto);
        printParameterMap.put("customerName", this.customerName);
        printParameterMap.put("total_rows", this.getRowCount());

        return printParameterMap;
    }
}
