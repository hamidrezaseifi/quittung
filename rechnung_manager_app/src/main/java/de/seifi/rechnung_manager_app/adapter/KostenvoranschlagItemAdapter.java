package de.seifi.rechnung_manager_app.adapter;

import de.seifi.rechnung_common.entities.KostenvoranschlagItemEntity;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagItemModel;

public class KostenvoranschlagItemAdapter extends AdapterBase<KostenvoranschlagItemEntity, KostenvoranschlagItemModel> {

	@Override
	KostenvoranschlagItemEntity toEntity(KostenvoranschlagItemModel model) {
		KostenvoranschlagItemEntity entity = null;
		if(model.isNew()) {
			entity = new KostenvoranschlagItemEntity(model.getProdukt(),
													 model.getOriginalNummer(),
													 model.getTeilNummer(),
													 model.getMarke(),
													 model.isBestellt(),
													 model.getPreis());
		} 
		else {
			entity = new KostenvoranschlagItemEntity(model.getId(),
													 model.getProdukt(),
													 model.getOriginalNummer(),
													 model.getTeilNummer(),
													 model.getMarke(),
													 model.isBestellt(),
													 model.getPreis(),
													 model.getCreated(),
													 model.getUpdated());
		}
		
		
		return entity;
	}

	@Override
	KostenvoranschlagItemModel toModel(KostenvoranschlagItemEntity entity) {
		KostenvoranschlagItemModel model = new KostenvoranschlagItemModel(entity.getId(),
																		  entity.getProdukt(),
																		  entity.getOriginalNummer(),
																		  entity.getTeilNummer(),
																		  entity.getMarke(),
																		  entity.getBestellt(),
																		  entity.getPreis(),
																		  entity.getCreated(),
																		  entity.getUpdated());
		
		return model;
	}

}
