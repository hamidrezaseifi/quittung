package de.seifi.rechnung_manager_app.enums;

public enum KostenvoranschlagStatus {
    OPEN(1),
    ACTIVE(5),
    DONE(10),
    ARCHIVED(20);

    private final int value;

    KostenvoranschlagStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static KostenvoranschlagStatus ofValue(int value){
        for(KostenvoranschlagStatus item: values()){
            if(item.value == value){
                return item;
            }
        }

        return null;
    }
}
