package org.evlis.lunamatic.utilities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

public class TranslationManager {
    private static TranslationManager instance;

    private final File translationsFolder;
    private final Map<String, FileConfiguration> translations = new HashMap<>();
    private final String defaultLanguage;

    public TranslationManager(File pluginFolder, String defaultLanguage) {
        this.translationsFolder = new File(pluginFolder, "translations");
        this.defaultLanguage = defaultLanguage;

        if (!translationsFolder.exists()) {
            translationsFolder.mkdirs();
        }
    }

    public static void initialize(File pluginFolder, String defaultLanguage) {
        instance = new TranslationManager(pluginFolder, defaultLanguage);
    }

    public static TranslationManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TranslationManager is not initialized. Call initialize() first.");
        }
        return instance;
    }

    public void loadTranslations() {
        File[] files = translationsFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String language = file.getName().replace(".yml", "");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                translations.put(language, config);
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
                if (entry.getName().startsWith("translations/") && entry.getName().endsWith(".yml")) {
                    String fileName = entry.getName().replace("translations/", "");
                    File translationFile = new File(translationsFolder, fileName);

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
        FileConfiguration config = translations.getOrDefault(defaultLanguage, translations.get(defaultLanguage));
        if (config != null) {
            String translate = config.getString(key);
            if (translate == null) {
                System.err.println("Translation missing! Try removing plugins/Lunamatic/translations folder. Key> "+key);
                return "langErr> "+key;
            }
            return translate;
        } else {
            System.err.println("Translation file is missing! Key> "+key);
            return "translaErr>: "+key;
        }

    }

    public boolean doesTranslationExist(String language) {
        if (translations.containsKey(language)) {
            return true;
        }

        File translationFile = new File(translationsFolder, language + ".yml");
        return translationFile.exists();
    }
}

