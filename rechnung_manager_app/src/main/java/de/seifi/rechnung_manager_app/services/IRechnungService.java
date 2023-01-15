package de.seifi.rechnung_manager_app.services;

import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.models.SearchFilterProperty;

import javax.transaction.Transactional;
import java.util.List;

public interface IRechnungService {
    @Transactional
    List<RechnungEntity> search(SearchFilterProperty searchFilterProperty);
}
