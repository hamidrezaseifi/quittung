package de.seifi.updater.models;

import java.util.List;

public class UpdateRoot {

    private String current;
    private List<UpdateVersion> versions;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public List<UpdateVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<UpdateVersion> versions) {
        this.versions = versions;
    }

    public UpdateVersion getCurrentVersion(){
        for(UpdateVersion version: versions){
            if(version.getStatus().equals("current")){
                return version;
            }
        }

        return null;
    }
}
