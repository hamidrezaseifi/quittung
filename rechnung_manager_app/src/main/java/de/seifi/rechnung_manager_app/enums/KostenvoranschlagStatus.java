package de.seifi.rechnung_manager_app.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum KostenvoranschlagStatus {
    NONE(0, "alle"),
    OPEN(1, "Öffen"),
    ACTIVE(5, "Aktiv"),
    DONE(10, "Fertig"),
    ARCHIVED(20, "Archiv");

    private final int value;

    private final String label;

    KostenvoranschlagStatus(int value, String label){
        this.value = value;
        this.label = label;
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

    @Override
    public String toString(){
        return this.label;

    }

    public static List<String> allLabels(){
        return Arrays.stream(values()).map(e -> e.label).collect(Collectors.toList());

    }

    public boolean isNotNone(){
        return this != NONE;

    }
}
