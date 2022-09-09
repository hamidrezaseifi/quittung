package de.seifi.rechnung_manager_app.models.print;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

import java.util.HashMap;
import java.util.Map;

public abstract class PrintJRDataSourceBase implements JRDataSource, IPrintJRDataSource {

    protected int rowIndex = -1;

    private Map<String, Object> printParameterMap = null;

    @Override
    public Map<String, Object> getPrintParameter() {
        if(this.printParameterMap == null){
            preparePrintParameterMap();
        }
        return printParameterMap;
    }

    @Override
    public boolean next() throws JRException {
        rowIndex += 1;
        return rowIndex < this.getRowCount();
    }

    protected abstract int getRowCount();

    protected abstract Float getTotalNeto();

    protected void preparePrintParameterMap() {
        this.printParameterMap = new HashMap<>();
        Float totalNeto = this.getTotalNeto();
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_ROWS, this.getRowCount());
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_NETO, totalNeto);
        printParameterMap.put(IPrintJRDataSource.PARAMETER_TOTAL_MWT, totalNeto * 19 / 100);
    }

}
