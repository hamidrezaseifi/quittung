package de.seifi.updater;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.seifi.updater.models.UpdateFile;
import de.seifi.updater.models.UpdateRoot;
import de.seifi.updater.models.UpdateVersion;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class UpdaterApp {


    public static void main(String[] args) throws IOException {

        UpdateRoot updateRoot = getUpdateRoot();

        //System.out.println(updateRoot.getCurrent());
        if(updateRoot != null){
            UpdateVersion updateVersion = updateRoot.getCurrentVersion();
            if(updateVersion != null){
                for(UpdateFile updateFile :updateVersion.getFiles()){
                    donwloadIfNotExists(updateFile);
                }
            }
        }

    }

    private static void donwloadIfNotExists(UpdateFile updateFile) throws IOException {
        String fileUrl = updateFile.getUrl();
        File downloadFile = getDownloadPath(updateFile);
        if(!downloadFile.getParentFile().exists()){
            System.out.println("Create folder " + downloadFile.getParentFile().getAbsoluteFile());
            downloadFile.getParentFile().mkdirs();
        }
        if(!downloadFile.exists()){
            System.out.println("Download file " + downloadFile.getAbsoluteFile());

            InputStream in = new URL(fileUrl).openStream();
            Files.copy(in, downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        }
    }

    public static File getDownloadPath(UpdateFile updateFile) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        Path configPath = Paths.get(s, updateFile.getDownload_path());

        return configPath.toFile();
    }


    private static UpdateRoot getUpdateRoot() {
        String updateString = getUpdateString();
        //System.out.println(updateString);

        UpdateRoot updateRoot = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            updateRoot = objectMapper.readValue(updateString, UpdateRoot.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return updateRoot;
    }

    private static String getUpdateString() {
        StringBuilder stringBuilder = new StringBuilder();

        try {

            URL url = new URL("http://cynical-conferences.000webhostapp.com/update.json");

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            in.close();

        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

        String updateSTring = stringBuilder.toString();
        return updateSTring;
    }

}
