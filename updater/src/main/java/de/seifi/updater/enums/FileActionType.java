package de.seifi.updater.enums;

public enum FileActionType {
    NONE(""),
    COPY("copy"),
    COPY_OVERWRITE("copy_overwrite"),
    DELETE("delete");

    private final String value;

    FileActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FileActionType fromValue(String value){
        if(value == null){
            return null;
        }

        for (FileActionType type: FileActionType.values()){
            if(type.getValue().equalsIgnoreCase(value)){
                return type;
            }
        }

        return null;
    }
}
