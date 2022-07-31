package de.seifi.rechnung_manager_app.services;

import de.seifi.rechnung_common.entities.ProduktEntity;
import de.seifi.rechnung_common.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.adapter.ProduktAdapter;
import de.seifi.rechnung_manager_app.models.ProduktModel;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProduktHelper {
    private static Map<String, ProduktModel> produktMap;
    private static List<ProduktModel> produktList;

    private static ProduktRepository produktRepository = null;
    
    private static final ProduktAdapter produktAdapter = new ProduktAdapter();
    

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

    public static ProduktModel getProdukt(String produkt) {
        if(produktMap == null){
            retreiveProduktList();
        }
        
        return produktMap.containsKey(produkt)? produktMap.get(produkt): null;
    }

    public static void retreiveProduktList() {
        List<ProduktEntity> entityList = getProduktRepository().findAll(Sort.by(Sort.Direction.ASC, "produktName"));
        produktList = entityList.stream().map(e -> produktAdapter.toModel(e)).collect(Collectors.toList());

        produktMap = produktList.stream().collect(Collectors.toMap(p -> p.getProduktName(), p -> p));
    }

    private static ProduktRepository getProduktRepository() {
        if(produktRepository == null){
            produktRepository = RechnungManagerSpringApp.applicationContext.getBean(ProduktRepository.class);
        }
        return produktRepository;
    }

    public static void add(String produkt, float preis){
        Optional<String> foundProdukt = ProduktHelper
                .getProduktMap().keySet().stream().filter(k -> k.toLowerCase().equals(produkt.toLowerCase())).findAny();
        ProduktEntity produktEntity = null;
        if(foundProdukt.isPresent()) {
        	ProduktModel model = ProduktHelper.getProduktMap().get(foundProdukt.get());
            produktEntity = produktAdapter.toEntity(model);
            produktEntity.setLastPreis(preis);
        }
        else {
            produktEntity = new ProduktEntity(produkt, preis);
        }
        getProduktRepository().save(produktEntity);
    }

	public static void save(ProduktModel produktModel) {
		// TODO Auto-generated method stub
		
	}
}
