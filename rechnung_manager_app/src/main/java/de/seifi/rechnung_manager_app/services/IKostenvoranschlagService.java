package de.seifi.rechnung_manager_app.services;

import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagSearchFilterProperty;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface IKostenvoranschlagService {

    List<KostenvoranschlagEntity> search(KostenvoranschlagSearchFilterProperty searchFilterProperty);

    void updateStatus(UUID id, KostenvoranschlagStatus done);
}
