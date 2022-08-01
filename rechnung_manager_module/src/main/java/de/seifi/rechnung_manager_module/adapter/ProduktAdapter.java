package de.seifi.rechnung_manager_module.adapter;

import de.seifi.rechnung_common.entities.ProduktEntity;
import de.seifi.rechnung_manager_module.models.ProduktModel;

public class ProduktAdapter extends AdapterBase<ProduktEntity, ProduktModel> {

	@Override
	public ProduktEntity toEntity(ProduktModel model) {
		ProduktEntity entity = null;
		if(model.isNew()) {
			entity = new ProduktEntity(model.getProduktName(), model.getLastPreis());
		} 
		else {
			entity = new ProduktEntity(model.getId(), model.getProduktName(), model.getLastPreis(), model.getUpdated());
		}
		
		
		return entity;
	}

	@Override
	public ProduktModel toModel(ProduktEntity entity) {
		ProduktModel model = new ProduktModel(entity.getId(), entity.getProduktName(), entity.getLastPreis(), entity.getCreated(), entity.getUpdated());
		
		return model;
	}

}
