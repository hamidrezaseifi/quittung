package de.seifi.rechnung_manager_app.data_service.impl;

import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.repositories.CustomerRepository;
import de.seifi.rechnung_manager_app.repositories.RechnungRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.Optional;

@Repository
public class RechnungDataHelper implements IRechnungDataHelper {

    @PersistenceContext
    private EntityManager entityManager;

    private final RechnungRepository rechnungRepository;

    public RechnungDataHelper(RechnungRepository rechnungRepository) {
        this.rechnungRepository = rechnungRepository;
    }

    @PostConstruct
    public void init(){

    }

    @Override
    public int getLastActiveRechnungNummer(){
        Optional<Integer> rNummerOptional = this.rechnungRepository.getMaxNummer(RechnungStatus.ACTIVE.getValue());

        int rNummer = rNummerOptional.isEmpty() ? 0 : rNummerOptional.get();

        return rNummer;
    }
}
