package de.seifi.rechnung_manager_module.data_service.impl;

import de.seifi.rechnung_common.repositories.RechnungRepository;

import de.seifi.rechnung_common.utils.RepositoryProvider;
import de.seifi.rechnung_manager_module.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_module.enums.RechnungStatus;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.Optional;

@Repository
public class RechnungDataHelper implements IRechnungDataHelper {

    private final RechnungRepository rechnungRepository;

    public RechnungDataHelper() {
        this.rechnungRepository = RepositoryProvider.getRechnungRepository();
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
