package de.seifi.rechnung_manager_module.enums;

public enum CustomerStatus {
    ACTIVE(1),
    ARCHIVED(10);

    private final int value;

    CustomerStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CustomerStatus ofValue(int value){
        for(CustomerStatus item: values()){
            if(item.value == value){
                return item;
            }
        }

        return null;
    }
}
