package org.evlis.lunamatic.utilities;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class TotoroDance {
    public void setRandomTickSpeed(World world, int tickSpeed) {
        world.setGameRule(org.bukkit.GameRule.RANDOM_TICK_SPEED, tickSpeed);
    }

    public void scheduleTimeSplit(Plugin plugin) {
        AtomicLong playerTime = new AtomicLong(1);
        AtomicReference<ScheduledTask> taskRef = new AtomicReference<>();
        GlobalRegionScheduler globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
        ScheduledTask task = globalRegionScheduler.runAtFixedRate(plugin, (t)-> {
            for (World world : Bukkit.getWorlds()) {
                // Check if the world has active players
                List<Player> playerList = world.getPlayers();
                if (playerList.isEmpty()) {
                    continue; // Skip worlds with no active players
                }
                world.setTime(12631);
                if (playerTime.get() >= 11368) {
                    world.setTime(23999);
                    for (Player p : playerList) {
                        p.resetPlayerTime();
                    }
                    taskRef.get().cancel();
                } else {
                    for (Player p : playerList) {
                        p.setPlayerTime(playerTime.get(), true);
                    }
                }
            }
            playerTime.addAndGet(1);
        }, 1L, 1L); // Check every 20 ticks (1 second)
        // Store our task reference...
        taskRef.set(task);
    }
}
