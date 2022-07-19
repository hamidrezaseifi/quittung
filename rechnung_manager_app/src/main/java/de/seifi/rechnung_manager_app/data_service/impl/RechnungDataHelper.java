package de.seifi.rechnung_manager_app.data_service.impl;

import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.repositories.QuittungRepository;
import de.seifi.rechnung_manager_app.repositories.RechnungRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class RechnungDataHelper implements IRechnungDataHelper {

    @PersistenceContext
    private EntityManager entityManager;

    private final RechnungRepository rechnungRepository;

    private final QuittungRepository quittungRepository;

    public RechnungDataHelper(RechnungRepository rechnungRepository,
                              QuittungRepository quittungRepository) {
        this.rechnungRepository = rechnungRepository;
        this.quittungRepository = quittungRepository;
    }

    @PostConstruct
    public void init(){

    }

    @Override
    public int getLastActiveRechnungNummer(){
        Optional<Integer> rNummerOptional = this.rechnungRepository.getMaxNummer(RechnungStatus.ACTIVE.getValue());
        Optional<Integer> qNummerOptional = this.quittungRepository.getMaxNummer(RechnungStatus.ACTIVE.getValue());

        int rNummer = rNummerOptional.isEmpty() ? 0 : rNummerOptional.get();
        int qNummer = qNummerOptional.isEmpty() ? 0 : qNummerOptional.get();

        if(qNummer > rNummer){
            return qNummer;
        }
        return rNummer;
    }
}
