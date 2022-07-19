package de.seifi.rechnung_manager_app.enums;

public enum RechnungStatus {
    ACTIVE(0),
    ARCHIVED(10);

    private final int value;

    RechnungStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RechnungStatus ofValue(int value){
        for(RechnungStatus item: values()){
            if(item.value == value){
                return item;
            }
        }

        return null;
    }
}
