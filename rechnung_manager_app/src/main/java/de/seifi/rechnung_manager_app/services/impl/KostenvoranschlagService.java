package de.seifi.rechnung_manager_app.services.impl;

import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagSearchFilterProperty;
import de.seifi.rechnung_manager_app.services.IKostenvoranschlagService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class KostenvoranschlagService implements IKostenvoranschlagService {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<KostenvoranschlagEntity> search(KostenvoranschlagSearchFilterProperty searchFilterProperty) {
        LocalDateTime tsFrom = searchFilterProperty.getFrom().atStartOfDay();
        LocalDateTime tsTo = searchFilterProperty.getTo().atTime(23, 59, 59);

        String queryString = "Select k from KostenvoranschlagEntity k where k.created between :from and :to";

        Integer nummer = null;
        try {
            nummer = Integer.parseInt(searchFilterProperty.getNummer());
            queryString += " and k.nummer=:nummer";
        }
        catch (Exception ex){

        }

        if(searchFilterProperty.getCustomerId() != null){

            queryString += " and k.customerId=:customerId";
        }

        if(searchFilterProperty.getStatus() > -1){

            queryString += " and k.status=:status";
        }

        queryString += " order by k." + searchFilterProperty.getOrderBy() + " " + searchFilterProperty.getOrderType();
        RechnungEntity e;

        Query query = entityManager.createQuery(queryString)
                                   .setParameter("from", tsFrom)
                                   .setParameter("to", tsTo);
        if(nummer != null){
            query.setParameter("nummer", nummer);
        }
        if(searchFilterProperty.getCustomerId() != null){
            query.setParameter("customerId", searchFilterProperty.getCustomerId());
        }

        if(searchFilterProperty.getStatus() > -1){

            query.setParameter("status", KostenvoranschlagStatus.ACTIVE.getValue());
        }

        List<KostenvoranschlagEntity> results = query.getResultList();

        return results;
    }
}
