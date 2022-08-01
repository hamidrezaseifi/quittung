package de.seifi.rechnung_manager_app.services.impl;

import de.seifi.rechnung_common.entities.ProduktEntity;
import de.seifi.rechnung_common.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.adapter.ProduktAdapter;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.services.IProduktService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


public class JpaProduktService implements IProduktService {
    private final Map<String, ProduktModel> produktMap;
    private final List<ProduktModel> produktList;

    private final ProduktRepository produktRepository;
    
    private final ProduktAdapter produktAdapter = new ProduktAdapter();

    public JpaProduktService(ProduktRepository produktRepository) {
        this.produktRepository = produktRepository;
        this.produktList = new ArrayList<>();
        this.produktMap = new HashMap<>();
    }

    public List<ProduktModel> getProduktList() {
        if(produktList.isEmpty()){
            retreiveProduktList();
        }
        return produktList;
    }

    public Map<String, ProduktModel> getProduktMap() {
        if(produktMap.isEmpty()){
            retreiveProduktList();
        }
        return produktMap;
    }

    public ProduktModel getProdukt(String produkt) {
        if(produktMap.isEmpty()){
            retreiveProduktList();
        }
        
        return produktMap.getOrDefault(produkt, null);
    }

    public void retreiveProduktList() {
        List<ProduktEntity> entityList = this.produktRepository.findAll(Sort.by(Sort.Direction.ASC, "produktName"));

        produktList.clear();
        produktList.addAll(entityList.stream().map(produktAdapter::toModel).collect(Collectors.toList()));

        produktMap.clear();
        produktMap.putAll(produktList.stream().collect(Collectors.toMap(ProduktModel::getProduktName, p -> p)));
    }

    public void add(String produkt, float preis){
        Optional<String> foundProdukt = produktMap.keySet().stream().filter(k -> k.equalsIgnoreCase(produkt)).findAny();
        ProduktEntity produktEntity = null;
        if(foundProdukt.isPresent()) {
        	ProduktModel model = produktMap.get(foundProdukt.get());
            produktEntity = produktAdapter.toEntity(model);
            produktEntity.setLastPreis(preis);
        }
        else {
            produktEntity = new ProduktEntity(produkt, preis);
        }
        this.produktRepository.save(produktEntity);
    }

	public void save(ProduktModel produktModel) {
		// TODO Auto-generated method stub
		
	}
}
