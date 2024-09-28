package org.evlis.lunamatic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.evlis.lunamatic.events.PlayerJoin;
import org.evlis.lunamatic.triggers.Scheduler;

public final class Lunamatic extends JavaPlugin {

    public static Boolean bloodMoonToday = false;
    public static Boolean bloodMoonNow = false;
    public PlayerJoin playerJoin;
    //public final Logger logger = this.getLogger();
    //public final File configFile = new File(this.getDataFolder(), "config.yml");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Scheduler schedule = new Scheduler();
        playerJoin = new PlayerJoin();
        Bukkit.getServer().getPluginManager().registerEvents(playerJoin, this);
        schedule.GetOmens(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
