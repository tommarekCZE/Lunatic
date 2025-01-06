package org.evlis.lunamatic.utilities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.evlis.lunamatic.GlobalVars;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LangManager {
    private static LangManager instance;

    private final File languageFolder;
    private final Map<String, FileConfiguration> langs = new HashMap<>();
    private final String defaultLanguage;

    public LangManager(File pluginFolder, String defaultLanguage) {
        this.languageFolder = new File(pluginFolder, "lang");
        this.defaultLanguage = GlobalVars.lang;

        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
        }
    }

    public static void initialize(File pluginFolder, String defaultLanguage) {
        instance = new LangManager(pluginFolder, defaultLanguage);
    }

    public static LangManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LangManager is not initialized. Call initialize() first.");
        }
        return instance;
    }

    public void loadTranslations() {
        File[] files = languageFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String language = file.getName().replace(".yml", "");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                langs.put(language, config);
            }
        }
    }

    public void saveDefaultTranslations() {
        try {
            // Get the plugin JAR file
            String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            JarFile jarFile = new JarFile(jarPath);

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                // Check for files in the /translations/ folder
                if (entry.getName().startsWith("lang/") && entry.getName().endsWith(".yml")) {
                    String fileName = entry.getName().replace("lang/", "");
                    File translationFile = new File(languageFolder, fileName);

                    if (!translationFile.exists()) {
                        try (InputStream inputStream = getClass().getResourceAsStream("/" + entry.getName())) {
                            if (inputStream != null) {
                                Files.copy(inputStream, translationFile.toPath());
                            }
                        }
                    }
                }
            }

            jarFile.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getTranslation(String key) {
        return getTranslation(key, defaultLanguage);
    }

    public String getTranslation(String key, String language) {
        FileConfiguration languageConfig = langs.get(language);
        String translation = null;

        if (languageConfig != null) {
            translation = languageConfig.getString(key);
        }

        if (translation == null) {
            // Attempt fallback to default language
            FileConfiguration defaultConfig = langs.get(defaultLanguage);
            if (defaultConfig != null) {
                translation = defaultConfig.getString(key);
            }
        }

        if (translation == null) {
            // Final fallback: return a placeholder or hardcoded string
            System.err.println("Translation missing! Key> " + key + " in language: " + language);
            translation = "langErr: " + key;
        }

        return translation;
    }

    public boolean doesTranslationExist(String language) {
        if (langs.containsKey(language)) {
            return true;
        }

        File translationFile = new File(languageFolder, language + ".yml");
        return translationFile.exists();
    }
}

