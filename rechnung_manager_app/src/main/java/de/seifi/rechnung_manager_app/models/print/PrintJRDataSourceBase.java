package de.seifi.rechnung_manager_app.models.print;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
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

    protected abstract Map<String, Object> preparePrintParameterMap();

    public void reset(){
        this.rowIndex = -1;

    }

}
