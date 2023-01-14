package de.seifi.rechnung_manager_app.services.impl;

import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.models.SearchFilterProperty;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class JpaRechnungService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PersistenceContext
    private EntityManager entityManager;

    public JpaRechnungService() {

    }

    @Transactional
    public List<RechnungEntity> search(SearchFilterProperty searchFilterProperty){
        LocalDateTime tsFrom = searchFilterProperty.getFrom().atStartOfDay();
        LocalDateTime tsTo = searchFilterProperty.getTo().atTime(23, 59, 59);

        String queryString = "Select r from RechnungEntity r where r.created between :from and :to and r.status=:status";

        Integer nummer = null;
        try {
            nummer = Integer.parseInt(searchFilterProperty.getNummer());
            queryString += " and r.nummer=:nummer";
        }
        catch (Exception ex){

        }

        if(searchFilterProperty.getCustomerId() != null){

            queryString += " and r.customerId=:customerId";
        }

        queryString += " order by r." + searchFilterProperty.getOrderBy() + " " + searchFilterProperty.getOrderType();
        RechnungEntity e;

        Query query = entityManager.createQuery(queryString)
                                   .setParameter("from", tsFrom)
                                   .setParameter("to", tsTo)
                                   .setParameter("status", RechnungStatus.ACTIVE.getValue());
        if(nummer != null){
            query.setParameter("nummer", nummer);
        }
        if(searchFilterProperty.getCustomerId() != null){
            query.setParameter("customerId", searchFilterProperty.getCustomerId());
        }

        List<RechnungEntity> results = query.getResultList();

        return results;
    }

}
