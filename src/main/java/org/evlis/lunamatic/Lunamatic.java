package org.evlis.lunamatic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.evlis.lunamatic.events.EntitySpawn;
import org.evlis.lunamatic.events.PlayerJoin;
import org.evlis.lunamatic.events.PlayerQuit;
import org.evlis.lunamatic.events.PlayerSleep;
import org.evlis.lunamatic.triggers.Scheduler;

public final class Lunamatic extends JavaPlugin {

    //private final ConsoleCommandSender consoleLogger = getServer().getConsoleSender();
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
        Scheduler schedule = new Scheduler();
        playerJoin = new PlayerJoin();
        playerSleep = new PlayerSleep();
        entitySpawn = new EntitySpawn();
        Bukkit.getServer().getPluginManager().registerEvents(playerJoin, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerQuit, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerSleep, this);
        Bukkit.getServer().getPluginManager().registerEvents(entitySpawn, this);
        schedule.GetOmens(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
