package de.seifi.rechnung_manager_app.enums;

public enum RechnungType {
    RECHNUNG(1),
    QUITTUNG(10);

    private final int value;

    RechnungType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RechnungType ofValue(int value){
        for(RechnungType item: values()){
            if(item.value == value){
                return item;
            }
        }

        return null;
    }
}
