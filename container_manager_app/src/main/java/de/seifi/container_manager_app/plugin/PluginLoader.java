package de.seifi.container_manager_app.plugin;

import de.seifi.rechnung_common.plugins.IViewLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PluginLoader {

    public static final String PLUGINS_FOLDER_NAME = "plugins";

    private final FilenameFilter pluginFilenameFilter = new FilenameFilter(){
        @Override
        public boolean accept(File dir, String name) {
            File file = Paths.get(dir.getAbsolutePath(), name).toFile();

            return name.toLowerCase().endsWith(".jar") && file.isFile();
        }
    };
    private Map<String, PluginItem> plugins;

    private final IViewLoader viewLoader;

    public PluginLoader(IViewLoader viewLoader) {
        this.viewLoader = viewLoader;
    }

    public void reloadPluginList() throws Exception {
        plugins = new HashMap<>();
        File pluginFolder = getPluginFolder();

        if(pluginFolder.exists() == false){
            pluginFolder.mkdir();
        }

        String[] files = pluginFolder.list(pluginFilenameFilter);
        List<File> pluginFiles = new ArrayList<>();
        for(String file: files){
            File pluginFile = Paths.get(pluginFolder.getAbsolutePath(), file).toFile();
            pluginFiles.add(pluginFile);

            PluginItem pluginItem = new PluginItem(pluginFile, this.viewLoader);
            plugins.put(file, pluginItem);

            //plugins.put(file, file);
        }


    }

    public Set<String> getPluginKeys(){
        return plugins.keySet();
    }

    public List<String> getPluginNames(){
        return null;
    }

    public PluginItem getPlugin(String key){
        return plugins.get(key);
    }

    public File getPluginFolder() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        Path configPath = Paths.get(s, PLUGINS_FOLDER_NAME);

        return configPath.toFile();
    }
}
