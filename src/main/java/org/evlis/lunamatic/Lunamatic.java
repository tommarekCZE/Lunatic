package org.evlis.lunamatic;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.evlis.lunamatic.events.EntitySpawn;
import org.evlis.lunamatic.events.PlayerJoin;
import org.evlis.lunamatic.events.PlayerSleep;
import org.evlis.lunamatic.triggers.Scheduler;

public final class Lunamatic extends JavaPlugin {

    public PlayerJoin playerJoin;
    public PlayerSleep playerSleep;
    public EntitySpawn entitySpawn;
    //private Integer defaultSpawnLimit;
    //public final Logger logger = this.getLogger();
    //public final File configFile = new File(this.getDataFolder(), "config.yml");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Scheduler schedule = new Scheduler();
        playerJoin = new PlayerJoin();
        playerSleep = new PlayerSleep();
        entitySpawn = new EntitySpawn();
        Bukkit.getServer().getPluginManager().registerEvents(playerJoin, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerSleep, this);
        Bukkit.getServer().getPluginManager().registerEvents(entitySpawn, this);
        schedule.GetOmens(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
