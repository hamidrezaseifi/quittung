package de.seifi.rechnung_manager_app.models.print;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class PrintJRDataSourceBase implements JRDataSource, IPrintJRDataSource {

    protected int rowIndex = -1;

    //private Map<String, Object> printParameterMap = null;

    @Override
    public Map<String, Object> getPrintParameter() throws IOException {
        return preparePrintParameterMap();
        //return printParameterMap;
    }

    @Override
    public boolean next() throws JRException {
        rowIndex += 1;
        return rowIndex < this.getRowCount();
    }

    protected abstract int getRowCount();

    protected abstract Float getTotalNeto();

    protected abstract String getCreateDate();

    protected Map<String, Object> preparePrintParameterMap() throws IOException {
        Map<String, Object> printParameterMap = new HashMap<>();
        Float totalNeto = this.getTotalNeto();
        printParameterMap.put(IPrintJRDataSource.RECHNUNG_CREATE_DATE, this.getCreateDate());
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_ROWS, this.getRowCount());
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_NETO, totalNeto);
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_MWT, totalNeto * 19 / 100);
        printParameterMap.put(IPrintJRDataSource.PARAMETER_LOGO_PATH, RechnungManagerFxApp.getLocalJasperPrintLogoPath());

        return printParameterMap;
    }

    public void reset(){
        this.rowIndex = -1;

    }

}
