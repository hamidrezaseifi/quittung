package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_common.entities.CustomerFahrzeugScheinEntity;

import java.util.UUID;

public class CustomerFahrzeugScheinModel {

    private UUID id;

    private UUID customerId;

    private String name;

    public CustomerFahrzeugScheinModel() {
    }

    public CustomerFahrzeugScheinModel(UUID id,
                                       UUID customerId,
                                       String name) {
        this.id = id;
        this.customerId = customerId;
        this.name = name;
    }

    public CustomerFahrzeugScheinModel(CustomerFahrzeugScheinEntity entity) {
        this.id = entity.getId();
        this.customerId = entity.getCustomerId();
        this.name = entity.getName();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
