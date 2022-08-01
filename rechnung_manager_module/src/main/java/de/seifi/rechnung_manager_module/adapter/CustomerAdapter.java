package de.seifi.rechnung_manager_module.adapter;


import de.seifi.rechnung_common.entities.CustomerEntity;
import de.seifi.rechnung_manager_module.models.CustomerModel;

public class CustomerAdapter extends AdapterBase<CustomerEntity, CustomerModel> {
	
	@Override
	public CustomerEntity toEntity(CustomerModel model) {
		CustomerEntity entity = null;
		if(model.isNew()) {
			entity = new CustomerEntity(model.getCustomerName(), model.getStreet(), model.getHouseNumber(), 
					model.getAddress2(), model.getPlz(), model.getCity(), model.getStatus().getValue());
		} 
		else {
			entity = new CustomerEntity(model.getId(), model.getCustomerName(), model.getStreet(), model.getHouseNumber(), 
					model.getAddress2(), model.getPlz(), model.getCity(), model.getStatus().getValue(), model.getUpdated());
		}

		return entity;
	}
	
	@Override
	public CustomerModel toModel(CustomerEntity entity) {
		CustomerModel model = new CustomerModel(entity.getId(), entity.getCustomerName(), entity.getStreet(), entity.getHouseNumber(), 
				entity.getAddress2(), entity.getPlz(), entity.getCity(), entity.getStatus(), entity.getCreated(), entity.getUpdated());
		
		return model;
	}

}
