package org.evlis.lunamatic;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.evlis.lunamatic.commands.LumaCommand;
import org.evlis.lunamatic.events.*;
import org.evlis.lunamatic.triggers.Scheduler;
import org.evlis.lunamatic.utilities.TranslationManager;

import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.HttpURLConnection;
import java.net.URL;

//getComponentLogger dosent output anything! Fixed that with getServer().getConsoleSender().sendMessage

public final class Lunamatic extends JavaPlugin {
    //private final ConsoleCommandSender consoleLogger = getServer().getConsoleSender();
    private static Lunamatic instance;
    public TranslationManager translationManager;

    public TimeSkip timeSkip;
    public PlayerJoin playerJoin;
    public PlayerQuit playerQuit;
    public PlayerSleep playerSleep;
    public EntitySpawn entitySpawn;
    //public final Logger logger = this.getLogger();
    //public final File configFile = new File(this.getDataFolder(), "config.yml");
    private static final String REQUIRED_VERSION = "1.21";
    public static final int REQUIRED_LANG_VER = 2;
    private static final String API_URL = "https://api.modrinth.com/v2/project/lunamatic/version";

    public void troubleShootLang() {
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.YELLOW + "---Language Troubleshoot Guide---\n1. If you recently updated Lunamatic, it is recommended to delete plugins/Lunamatic/translations folder. (To allow Lunamatic to use recent translation files)\n2. Check for wrong entered language name in Lunamatic/config.yml\n3. If you were messing with the translations files, you might forget to update the lang_ver value.");
    }

    public void troubleShootConfig() {
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.YELLOW + "---Config Troubleshoot Guide---\n1. If you recently updated Lunamatic, it is recommended to delete plugins/Lunamatic/config.yml file. (To allow Lunamatic to use recent config version)\n2. You might did some typos in Lunamatic/config.yml");
    }

    @Override
    public void onEnable() {
        // Begin Initialization
        this.getComponentLogger().debug(Component.text("Loading Lunamatic...", NamedTextColor.GOLD));

        // Assing instance variable
        instance = this;

        // Get versions
        String serverVersion = getServer().getMinecraftVersion();
        String currentVersion = this.getPluginMeta().getVersion();
        // Check server version, log error if not supported.
        if (!serverVersion.startsWith(REQUIRED_VERSION)) {
            getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.RED + "Unsupported server version detected! Expected ver: "+REQUIRED_VERSION+", Your version: "+serverVersion);
            getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.YELLOW + "Expect unexpected behaviours or crashes!, It is recommended to use expected version!");
        }
        // Config Initialization
        saveDefaultConfig();
        loadGlobalConfig();

        // Load translations
        TranslationManager.initialize(getDataFolder(),GlobalVars.lang);
        translationManager = TranslationManager.getInstance();
        translationManager.saveDefaultTranslations();
        translationManager.loadTranslations();

        if (!translationManager.doesTranslationExist(GlobalVars.lang)) {
            getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.RED + GlobalVars.lang + " language does NOT exists! Disabling plugin.");
            troubleShootLang();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }


        if (Integer.parseInt(translationManager.getTranslation("lang_ver")) != REQUIRED_LANG_VER) {
            getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.RED + "Unsupported language version! Disabling plugin. Expected lang ver: "+REQUIRED_LANG_VER);
            troubleShootLang();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.GREEN + translationManager.getTranslation("lang_load_success"));

        // Update check
        if (GlobalVars.checkUpdates) {
            checkForUpdates(currentVersion);
        }
        // Class Initialization
        Scheduler schedule = new Scheduler();
        timeSkip = new TimeSkip();
        playerJoin = new PlayerJoin();
        playerQuit = new PlayerQuit();
        playerSleep = new PlayerSleep();
        entitySpawn = new EntitySpawn();
        Bukkit.getServer().getPluginManager().registerEvents(timeSkip, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerJoin, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerQuit, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerSleep, this);
        Bukkit.getServer().getPluginManager().registerEvents(entitySpawn, this);
        registerCommands();

        schedule.GetOmens(this);
        // Notify of successful plugin start
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.GOLD + translationManager.getTranslation("plugin_success_load") + currentVersion);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().warning("Lunamatic has been disabled.");
    }

    public void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new LumaCommand(this));
    }

    public void loadGlobalConfig() {
        try {
            reloadConfig();
            // plugin vars
            GlobalVars.checkUpdates = getConfig().getBoolean("checkForUpdates");
            GlobalVars.lang = getConfig().getString("lang");
            // moons enabled
            GlobalVars.disabledWorlds = getConfig().getStringList("disabledWorlds");
            GlobalVars.fullMoonEnabled = getConfig().getBoolean("fullMoonEnabled");
            GlobalVars.fullMoonEnabled = getConfig().getBoolean("newMoonEnabled");
            GlobalVars.fullMoonEnabled = getConfig().getBoolean("harvestMoonEnabled");
            GlobalVars.fullMoonEnabled = getConfig().getBoolean("bloodMoonEnabled");
            // moon chances
            GlobalVars.harvestMoonDieSides = getConfig().getInt("bloodMoonDieSides");
            GlobalVars.bloodMoonDieSides = getConfig().getInt("harvestMoonDieSides");
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration! Disabling plugin. Error: " + e.getMessage());
            troubleShootConfig();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void checkForUpdates(String currentVersionString) {
        getLogger().info(translationManager.getTranslation("update_check"));
        try {
            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Parse the response
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // Get the first item in the JSON array
            JsonElement latestVersionElement = jsonArray.get(0);
            String latestVersionString = latestVersionElement.getAsJsonObject().get("version_number").getAsString();

            // Compare versions
            ModuleDescriptor.Version latestVersion = ModuleDescriptor.Version.parse(latestVersionString);
            ModuleDescriptor.Version currentVersion = ModuleDescriptor.Version.parse(currentVersionString);
            if (currentVersion.compareTo(latestVersion) < 0) {
                getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.GOLD + translationManager.getTranslation("update_found").replace("%a",latestVersionString).replace("%b",currentVersionString));
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.RED + translationManager.getTranslation("update_error"));
        }
    }

    public static Lunamatic getInstance() {
        return instance;
    }
}
