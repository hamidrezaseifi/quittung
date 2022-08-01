package de.seifi.rechnung_manager_module.adapter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AdapterBase<E,M> {

	abstract E toEntity(M model);

	abstract M toModel(E entity);
	
	public List<E> toEntityList(Collection<M> modelList){
		List<E> entityList = modelList.stream().map(m -> toEntity(m)).collect(Collectors.toList());
		
		return entityList;
	}
	
	public List<M> toModelList(Collection<E> entityList){
		List<M> modelList = entityList.stream().map(m -> toModel(m)).collect(Collectors.toList());
		
		return modelList;
	}
}
