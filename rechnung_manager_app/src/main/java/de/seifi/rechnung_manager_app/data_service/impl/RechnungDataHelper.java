package de.seifi.rechnung_manager_app.data_service.impl;

import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_common.repositories.RechnungRepository;
import de.seifi.rechnung_common.repositories.KostenvoranschlagRepository;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class RechnungDataHelper implements IRechnungDataHelper {

    private final RechnungRepository rechnungRepository;

    private final KostenvoranschlagRepository kostenvoranschlagRepository;

    public RechnungDataHelper(RechnungRepository rechnungRepository,
                              KostenvoranschlagRepository kostenvoranschlagRepository) {
        this.rechnungRepository = rechnungRepository;
        this.kostenvoranschlagRepository = kostenvoranschlagRepository;
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

    @Override
    public String getNewActivevorschlagNummer() {

        Optional<KostenvoranschlagEntity> lastKostenvoranschlagOptional = this.kostenvoranschlagRepository.findFirstByOrderByCreatedDesc();
        int currentYear = LocalDate.now().getYear();

        if(lastKostenvoranschlagOptional.isPresent()){
            String lastNummer = lastKostenvoranschlagOptional.get().getNummer();
            String[] parts = lastNummer.split("/");
            if(parts[0].equals(currentYear + "")){
                int lastNumInt = Integer.parseInt(parts[1]);
                int newNumm = lastNumInt + 1;
                return currentYear + "/" + newNumm;
            }
        }

        return currentYear + "/1";
    }
}
