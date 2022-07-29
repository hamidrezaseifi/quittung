package de.seifi.rechnung_manager_app.adapter;

import de.seifi.rechnung_common.entities.RechnungItemEntity;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;

public class RechnungItemAdapter extends AdapterBase<RechnungItemEntity, RechnungItemModel> {

	@Override
	RechnungItemEntity toEntity(RechnungItemModel model) {
		RechnungItemEntity entity = null;
		if(model.isNew()) {
			entity = new RechnungItemEntity(model.getProdukt(), model.getArtikelNummer(), model.getMenge(), model.getPreis());
		} 
		else {
			entity = new RechnungItemEntity(model.getId(), model.getProdukt(), model.getArtikelNummer(), model.getMenge(), model.getPreis(), 
					model.getUpdated());
		}
		
		
		return entity;
	}

	@Override
	RechnungItemModel toModel(RechnungItemEntity entity) {
		RechnungItemModel model = new RechnungItemModel(entity.getId(), entity.getProdukt(), entity.getArtikelNummer(), entity.getMenge(), 
				entity.getPreis(), entity.getCreated(), entity.getUpdated());
		
		return model;
	}

}
