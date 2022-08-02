package de.seifi.container_manager_app.plugin;

import de.seifi.rechnung_common.plugins.IPluginInfo;
import de.seifi.rechnung_common.plugins.IViewLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PluginItem {

    public static final String PLUGIN_INFO_PACKAGE_NAME = "de.seifi.rechnung_manager_plugin.PluginInfo";

    private final String filePath;

    private URLClassLoader urlClassLoader;

    private String title = "not_Set";

    private Menu topMenu = null;

    private final IViewLoader viewLoader;

    private final List<String> classNames = new ArrayList<>();

    public PluginItem(File pluginFile, IViewLoader viewLoader) throws Exception {
        this.filePath = pluginFile.getAbsolutePath();
        this.viewLoader = viewLoader;

        URL[] jarUrls = new URL[]{pluginFile.toURI().toURL()};
        URLClassLoader urlClassLoader = new URLClassLoader(jarUrls, this.getClass().getClassLoader());

        this.urlClassLoader = urlClassLoader;
        Thread.currentThread().setContextClassLoader(this.urlClassLoader);

        loadAllClasses();

        Class<?> pluginInfoClass = this.urlClassLoader.loadClass(PLUGIN_INFO_PACKAGE_NAME);

        IPluginInfo instance = (IPluginInfo)pluginInfoClass.getDeclaredConstructors()[0].newInstance();

        this.title = instance.getPluginName();

        this.topMenu = instance.getTopMenu();
        this.topMenu.setUserData(this.viewLoader);
        for(MenuItem menuItem: this.topMenu.getItems()){
            menuItem.setUserData(this.viewLoader);
        }
    }

    private void loadAllClasses() throws IOException, ClassNotFoundException {
        ZipInputStream zip = new ZipInputStream(new FileInputStream(this.filePath));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                // This ZipEntry represents a class. Now, what class does it represent?
                String className = entry.getName().replace('/', '.'); // including ".class"
                String classFullPath = className.substring(0, className.length() - ".class".length());
                Class<?> inputClass = this.urlClassLoader.loadClass(classFullPath);
                Class.forName(classFullPath, false, inputClass.getClassLoader());
                this.classNames.add(classFullPath);
            }
        }
    }

    public Menu getTopMenu() {

        return topMenu;
    }

    public String getTitle() {

        return this.title;
    }

    public String getFilePath() {
        return filePath;
    }
}
