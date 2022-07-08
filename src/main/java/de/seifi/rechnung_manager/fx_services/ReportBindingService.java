package de.seifi.rechnung_manager.fx_services;


import de.seifi.rechnung_manager.entities.ProduktEntity;
import de.seifi.rechnung_manager.entities.RechnungEntity;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.models.ReportItemModel;
import de.seifi.rechnung_manager.repositories.ProduktRepository;
import de.seifi.rechnung_manager.repositories.RechnungRepository;
import de.seifi.rechnung_manager.models.RechnungItemProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private ObservableList<ReportItemModel> rechnungItems;

    private BooleanProperty disablePrint;

    private boolean isDirty;
    
    public ReportBindingService(ProduktRepository produktRepository,
    		final RechnungRepository rechnungRepository) {
    	
    	CURRENT_INSTANCE = this;

    	this.produktRepository = produktRepository;
    	this.rechnungRepository = rechnungRepository;

        disablePrint = new SimpleBooleanProperty(false);
        
        rechnungItems = FXCollections.observableArrayList();
        
        retreiveProduktMap();
        
        search();

    }

    private void retreiveProduktMap() {
    	List<ProduktEntity> entityList = produktRepository.findAll(Sort.by(Sort.Direction.ASC, "produktName"));
    	produktList = entityList.stream().map(e -> e.toModel()).collect(Collectors.toList());
    	
    	produktMap = produktList.stream().collect(Collectors.toMap(p -> p.getProduktName(), p -> p));
    }

    public ObservableList<ReportItemModel> getRechnungItems() {
        return rechnungItems;
    }

	public void search() {
		rechnungItems.clear();

        List<RechnungEntity> entityList = this.rechnungRepository.findAll();
        List<ReportItemModel> modelList = entityList.stream().map(e -> new ReportItemModel(e.toModel())).collect(Collectors.toList());

        rechnungItems.addAll(modelList);

        isDirty = false;
	}

	public boolean isDirty() {
		return isDirty;
	}

	
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		
		this.disablePrint.set(isDirty);
	}

	public BooleanProperty getDisablePrintProperty() {
		return disablePrint;
	}

	public List<ProduktModel> getProduktList() {
		return produktList;
	}

	
	
}
