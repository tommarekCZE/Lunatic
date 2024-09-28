package org.evlis.lunatic.triggers;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evlis.lunatic.Lunatic;
import org.evlis.lunatic.utilities.PlayerMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Scheduler {

    public void GetOmens(Plugin plugin) {
        GlobalRegionScheduler globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
        globalRegionScheduler.runAtFixedRate(plugin, (t)-> {
            for (World world : Bukkit.getWorlds()) {
                // Check if the world has active players
                List<Player> playerList = world.getPlayers();
                if (playerList.isEmpty()) {
                    continue; // Skip worlds with no active players
                }

                long time = world.getTime();
                // Check if it's the start of the day (0 ticks, 6am)
                if (time >= 0 && time < 20) {
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    Lunatic.bloodMoonToday = false;
                    Lunatic.bloodMoonNow = false;
                    if (moonPhase == MoonPhase.FULL_MOON) {
                        // TO-DO: Implement Harvest Moon
                        PlayerMessage.Send(playerList, "Full moon tonight.", NamedTextColor.YELLOW);
                    } else if (moonPhase == MoonPhase.NEW_MOON) {
                        Random r = new Random();
                        // Do a dice roll to check if the players are THAT unlucky..
                        int chance = r.nextInt(2);
                        if (chance == 0) {
                            Lunatic.bloodMoonToday = true;
                            PlayerMessage.Send(playerList, "Blood moon tonight.", NamedTextColor.DARK_RED);
                        } else {
                            PlayerMessage.Send(playerList, "New moon tonight.", NamedTextColor.DARK_GRAY);
                        }
                    }
                } // Execute at exactly dusk
                if (time >= 12610 && time < 12630) {
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    for (Player p : playerList) {
                        NightEffects.ApplyMoonlight(p, moonPhase, (24000 - (int)time));
                    }
                    if (Lunatic.bloodMoonToday) {
                        Lunatic.bloodMoonNow = true;
                    }
                }
            }
        }, 1L, 20L); // Check every 20 ticks (1 second)
    }
}
