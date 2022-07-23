package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_manager_app.entities.ProduktEntity;
import de.seifi.rechnung_manager_app.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.repositories.RechnungRepository;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

public class ReportBindingService {
	
	public static ReportBindingService CURRENT_INSTANCE = null;
	
    private int INITIAL_ITEMS = 10;
    
    private final ProduktRepository produktRepository;
    
    private final RechnungRepository rechnungRepository;
    
	    
    private Map<String, ProduktModel> produktMap;
    private List<ProduktModel> produktList;

    private ObservableList<ReportItemModel> rechnungItems;

    private boolean isDirty;

    private SearchFilterProperty searchFilterProperty;
    
    public ReportBindingService(ProduktRepository produktRepository,
    		final RechnungRepository rechnungRepository) {
    	
    	CURRENT_INSTANCE = this;

    	this.produktRepository = produktRepository;
    	this.rechnungRepository = rechnungRepository;
        
        rechnungItems = FXCollections.observableArrayList();

        this.searchFilterProperty = new SearchFilterProperty();
        this.searchFilterProperty.setFrom(LocalDate.now().minusMonths(2));
        this.searchFilterProperty.setTo(LocalDate.now());

        retreiveProduktMap();

    }

    private void retreiveProduktMap() {
    	List<ProduktEntity> entityList = produktRepository.findAll(Sort.by(Sort.Direction.ASC, "produktName"));
    	produktList = entityList.stream().map(e -> e.toModel()).collect(Collectors.toList());
    	
    	produktMap = produktList.stream().collect(Collectors.toMap(p -> p.getProduktName(), p -> p));
    }

    public ObservableList<ReportItemModel> getReportItems() {
        return rechnungItems;
    }

	public void search() {
		rechnungItems.clear();
		LocalDateTime tsFrom = searchFilterProperty.getFrom().atStartOfDay();
        LocalDateTime tsTo = searchFilterProperty.getTo().atTime(23, 59, 59);
        List<RechnungEntity> entityList;
        Integer nummer = null;
        try {
            nummer = Integer.parseInt(searchFilterProperty.getNummer());
        }
        catch (Exception ex){

        }
        if(nummer != null){
            entityList = this.rechnungRepository.search(tsFrom, tsTo, nummer);
        }
        else{
            entityList = this.rechnungRepository.search(tsFrom, tsTo);
        }

        List<ReportItemModel> modelList = entityList.stream().map(e -> new ReportItemModel(e.toModel())).collect(Collectors.toList());

        rechnungItems.addAll(modelList);

        isDirty = false;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public List<ProduktModel> getProduktList() {
		return produktList;
	}

    public SearchFilterProperty getSearchFilterProperty() {
        return searchFilterProperty;
    }
}
