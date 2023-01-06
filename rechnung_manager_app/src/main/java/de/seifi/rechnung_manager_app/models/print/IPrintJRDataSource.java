package de.seifi.rechnung_manager_app.models.print;

import java.io.IOException;
import java.util.Map;

public interface IPrintJRDataSource {
    static final String PARAMETER_TOTAL_NETO = "total_neto";
    static final String PARAMETER_TOTAL_MWT = "mwt";
    static final String PARAMETER_TOTAL_ROWS = "total_rows";
    static final String PARAMETER_LOGO_PATH = "logo_path";
    static final String RECHNUNG_CREATE_DATE = "rechnung_create";

    Map<String, Object> getPrintParameter() throws IOException;
}
