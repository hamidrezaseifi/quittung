package de.seifi.rechnung_manager_app.models.print;

import net.sf.jasperreports.engine.JRField;

public interface IPrintJRRow {

    Float getGesamt();

    Object getFieldValue(String field);
}
