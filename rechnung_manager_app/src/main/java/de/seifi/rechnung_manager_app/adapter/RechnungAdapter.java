package de.seifi.rechnung_manager_app.adapter;

import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.models.RechnungModel;

public class RechnungAdapter extends AdapterBase<RechnungEntity, RechnungModel> {

	@Override
	public RechnungEntity toEntity(RechnungModel model) {
		RechnungEntity entity = null;
		if(model.isNew()) {
			entity = new RechnungEntity(model.getCustomerId(), model.getNummer(), model.getRechnungCreate(), model.getLiferDate(),
										model.getRechnungVersion(), model.getRechnungType().getValue(), model.getStatus().getValue(),
										model.getUserId());
		} 
		else {
			entity = new RechnungEntity(model.getId(), model.getCustomerId(), model.getNummer(), model.getRechnungCreate(),
										model.getLiferDate(), model.getRechnungVersion(), model.getRechnungType().getValue(),
										model.getStatus().getValue(), model.getUserId(), model.getUpdated());
		}
		
		RechnungItemAdapter itemAdapter = new RechnungItemAdapter();
		entity.setItems(itemAdapter.toEntityList(model.getItems()));
		
		return entity;
	}

	@Override
	public RechnungModel toModel(RechnungEntity entity) {
		RechnungModel model = new RechnungModel(entity.getId(), entity.getCustomerId(), entity.getNummer(), entity.getRechnungCreate(), 
				entity.getLiferDate(), entity.getRechnungVersion(), entity.getRechnungType(), entity.getStatus(), entity.getUserId(),
												entity.getCreated(), entity.getUpdated());
		
		RechnungItemAdapter itemAdapter = new RechnungItemAdapter();
		model.setItems(itemAdapter.toModelList(entity.getItems()));
		
		return model;
	}

}
