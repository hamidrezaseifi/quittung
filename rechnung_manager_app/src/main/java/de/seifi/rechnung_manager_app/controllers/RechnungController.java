package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_manager_app.enums.RechnungType;

public class RechnungController extends RechnungControllerBase {

    public RechnungController() {
        super(RechnungType.RECHNUNG);
    }
}
