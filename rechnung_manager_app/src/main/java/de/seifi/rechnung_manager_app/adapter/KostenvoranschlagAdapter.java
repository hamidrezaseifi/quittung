package de.seifi.rechnung_manager_app.adapter;

import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagModel;

public class KostenvoranschlagAdapter extends AdapterBase<KostenvoranschlagEntity, KostenvoranschlagModel> {

	private final KostenvoranschlagItemAdapter itemAdapter = new KostenvoranschlagItemAdapter();

	@Override
	public KostenvoranschlagEntity toEntity(KostenvoranschlagModel model) {
		KostenvoranschlagEntity entity;
		if(model.isNew()) {
			entity = new KostenvoranschlagEntity(model.getCustomerId(),
												 model.getNummer(),
												 model.getSchluesselNummer(),
												 model.getFahrgestellNummer(),
												 model.getFahrzeugSchein(),
												 model.getStatus().getValue());
		} 
		else {
			entity = new KostenvoranschlagEntity(model.getId(),
												 model.getCustomerId(),
												 model.getNummer(),
												 model.getSchluesselNummer(),
												 model.getFahrgestellNummer(),
												 model.getFahrzeugSchein(),
												 model.getStatus().getValue(),
												 model.getCreated(),
												 model.getUpdated());
		}


		entity.setItems(itemAdapter.toEntityList(model.getItems()));
		
		return entity;
	}

	@Override
	public KostenvoranschlagModel toModel(KostenvoranschlagEntity entity) {
		KostenvoranschlagModel model = new KostenvoranschlagModel(entity.getId(),
																  entity.getCustomerId(),
																  entity.getNummer(),
																  entity.getSchluesselNummer(),
																  entity.getFahrgestellNummer(),
																  entity.getFahrzeugSchein(),
																  KostenvoranschlagStatus.ofValue(entity.getStatus()),
																  entity.getCreated(),
																  entity.getUpdated());
		
		model.setItems(itemAdapter.toModelList(entity.getItems()));
		
		return model;
	}

}
