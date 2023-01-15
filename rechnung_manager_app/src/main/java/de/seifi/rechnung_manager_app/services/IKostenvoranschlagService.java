package de.seifi.rechnung_manager_app.services;

import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagSearchFilterProperty;

import javax.transaction.Transactional;
import java.util.List;

public interface IKostenvoranschlagService {

    List<KostenvoranschlagEntity> search(KostenvoranschlagSearchFilterProperty searchFilterProperty);
}
