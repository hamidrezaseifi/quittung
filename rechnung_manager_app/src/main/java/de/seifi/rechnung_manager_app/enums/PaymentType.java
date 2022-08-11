package de.seifi.rechnung_manager_app.enums;

public enum PaymentType {
    NOT_SET(0, ""),
    CASH(1, "Bar"),
    CARD(5, "Card");

    private final int value;

    private final String title;

    PaymentType(int value, String title){
        this.value = value;
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public static PaymentType ofValue(int value){
        for(PaymentType item: values()){
            if(item.value == value){
                return item;
            }
        }

        return null;
    }

    @Override
    public String toString(){
        return title;
    }
}
