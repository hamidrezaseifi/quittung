package de.seifi.rechnung_manager_app.enums;

public enum RechnungType {
    RECHNUNG(1, "Rechnung"),
    QUITTUNG(10, "Quittung");

    private final int value;

    private final String title;

    RechnungType(int value, String title){
        this.value = value;
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
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
