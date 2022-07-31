package de.seifi.rechnung_manager_app.services;

import de.seifi.rechnung_manager_app.models.ProduktModel;

import java.util.List;
import java.util.Map;


public interface IProduktService {

    List<ProduktModel> getProduktList();

    Map<String, ProduktModel> getProduktMap();

    ProduktModel getProdukt(String produkt);

    void retreiveProduktList();

    void add(String produkt, float preis);

	void save(ProduktModel produktModel);
}
