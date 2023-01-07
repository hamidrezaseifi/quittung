package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_common.entities.CustomerFahrzeugScheinEntity;

import java.util.UUID;

public class CustomerFahrzeugScheinModel {

    private UUID id;

    private UUID customerId;

    private String name;

    private String imagePath;

    private byte[] imageBytes;

    public CustomerFahrzeugScheinModel() {
    }

    public CustomerFahrzeugScheinModel(CustomerFahrzeugScheinEntity entity) {
        this.id = entity.getId();
        this.customerId = entity.getCustomerId();
        this.name = entity.getName();
        this.imageBytes = entity.getImageBytes();
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public String toString(){
        return name;
    }
}
