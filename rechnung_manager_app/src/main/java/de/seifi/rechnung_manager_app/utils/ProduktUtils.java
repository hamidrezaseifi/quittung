package de.seifi.rechnung_manager_app.utils;

import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.entities.ProduktEntity;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.repositories.ProduktRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProduktUtils {
    private static Map<String, ProduktModel> produktMap;
    private static List<ProduktModel> produktList;

    private static ProduktRepository produktRepository = null;

    public static List<ProduktModel> getProduktList() {
        if(produktList == null){
            retreiveProduktList();
        }
        return produktList;
    }

    public static Map<String, ProduktModel> getProduktMap() {
        if(produktMap == null){
            retreiveProduktList();
        }
        return produktMap;
    }

    public static void retreiveProduktList() {
        List<ProduktEntity> entityList = getProduktRepository().findAll(Sort.by(Sort.Direction.ASC, "produktName"));
        produktList = entityList.stream().map(e -> e.toModel()).collect(Collectors.toList());

        produktMap = produktList.stream().collect(Collectors.toMap(p -> p.getProduktName(), p -> p));
    }

    private static ProduktRepository getProduktRepository() {
        if(produktRepository == null){
            produktRepository = RechnungManagerSpringApp.applicationContext.getBean(ProduktRepository.class);
        }
        return produktRepository;
    }

    public static void add(String produkt, float preis){
        Optional<String> foundProdukt = ProduktUtils.getProduktMap().keySet().stream().filter(k -> k.toLowerCase().equals(produkt.toLowerCase())).findAny();
        ProduktEntity produktEntity = null;
        if(foundProdukt.isPresent()) {
            produktEntity = ProduktUtils.getProduktMap().get(foundProdukt.get()).toEntity();
            produktEntity.setLastPreis(preis);
        }
        else {
            produktEntity = new ProduktEntity(produkt, preis);
        }
        getProduktRepository().save(produktEntity);
    }
}
