package org.evlis.lunamatic;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.evlis.lunamatic.commands.LumaCommand;
import org.evlis.lunamatic.events.*;
import org.evlis.lunamatic.triggers.Scheduler;

import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Lunamatic extends JavaPlugin {

    //private final ConsoleCommandSender consoleLogger = getServer().getConsoleSender();
    public TimeSkip timeSkip;
    public PlayerJoin playerJoin;
    public PlayerQuit playerQuit;
    public PlayerSleep playerSleep;
    public EntitySpawn entitySpawn;
    //public final Logger logger = this.getLogger();
    //public final File configFile = new File(this.getDataFolder(), "config.yml");
    private static final String REQUIRED_VERSION = "1.21";
    private static final String API_URL = "https://api.modrinth.com/v2/project/lunamatic/version";

    @Override
    public void onEnable() {
        // Begin Initialization
        this.getComponentLogger().debug(Component.text("Loading Lunamatic...", NamedTextColor.GOLD));
        // Get versions
        String serverVersion = getServer().getMinecraftVersion();
        String currentVersion = this.getPluginMeta().getVersion();
        // Check server version, log error if not supported.
        if (!serverVersion.startsWith(REQUIRED_VERSION)) {
            this.getComponentLogger().error(Component.text("Unsupported server version: " + serverVersion, NamedTextColor.RED));
        }
        // Config Initialization
        saveDefaultConfig();
        loadGlobalConfig();
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
        this.getComponentLogger().debug(Component.text("Successfully enabled Lunamatic v" + currentVersion, NamedTextColor.GOLD));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getComponentLogger().debug(Component.text("Lunamatic has been disabled.", NamedTextColor.GOLD));
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
            getLogger().severe("Failed to load configuration: " + e.getMessage());
        }
    }

    public void checkForUpdates(String currentVersionString) {
        getLogger().info("Checking for updates...");
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
                this.getComponentLogger().debug(Component.text("New Version " + latestVersionString + " available (you are on v" +  currentVersionString + ")! Download here: https://modrinth.com/plugin/lunamatic", NamedTextColor.GOLD));
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            this.getComponentLogger().error(Component.text("Unable to check for updates: " + e.getMessage(), NamedTextColor.RED));
        }
    }
}
