package de.seifi.rechnung_manager_app.models.print;

import java.util.Map;

public interface IPrintJRDataSource {
    static final String PARAMETER_TOTAL_NETO = "total_neto";
    static final String PARAMETER_TOTAL_MWT = "mwt";
    static final String PARAMETER_TOTAL_ROWS = "total_rows";

    Map<String, Object> getPrintParameter();
}
