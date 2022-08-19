package de.seifi.updater.models;

import java.util.List;

public class UpdateVersion {

    private String version;
    private String status;
    private List<UpdateFile> files;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UpdateFile> getFiles() {
        return files;
    }

    public void setFiles(List<UpdateFile> files) {
        this.files = files;
    }
}
