package org.evlis.lunamatic;

import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.evlis.lunamatic.commands.LumaCommand;
import org.evlis.lunamatic.events.*;
import org.evlis.lunamatic.triggers.Scheduler;

public final class Lunamatic extends JavaPlugin {

    //private final ConsoleCommandSender consoleLogger = getServer().getConsoleSender();
    public TimeSkip timeSkip;
    public PlayerJoin playerJoin;
    public PlayerQuit playerQuit;
    public PlayerSleep playerSleep;
    public EntitySpawn entitySpawn;
    //public final Logger logger = this.getLogger();
    //public final File configFile = new File(this.getDataFolder(), "config.yml");

    @Override
    public void onEnable() {
        //consoleLogger.sendMessage(MiniMessage.miniMessage().deserialize(""));
        // Plugin startup logic
        saveDefaultConfig();
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
        loadGlobalConfig();
        schedule.GetOmens(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getComponentLogger().debug(Component.text("Lunamatic has been disabled."));
    }

    public void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new LumaCommand(this));
    }

    public void loadGlobalConfig() {
        try {
            reloadConfig();
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
}
