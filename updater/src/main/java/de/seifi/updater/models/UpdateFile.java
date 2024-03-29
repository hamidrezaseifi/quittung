package de.seifi.updater.models;

import de.seifi.updater.enums.FileActionType;

public class UpdateFile {

    private String name;
    private String url;
    private String action;
    private String download_path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public FileActionType getActionType(){
        return FileActionType.fromValue(action);
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDownload_path() {
        return download_path;
    }

    public void setDownload_path(String download_path) {
        this.download_path = download_path;
    }
}
