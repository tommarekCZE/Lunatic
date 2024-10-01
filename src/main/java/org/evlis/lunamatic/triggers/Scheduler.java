package org.evlis.lunamatic.triggers;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.evlis.lunamatic.utilities.TotoroDance;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Scheduler {

    public void GetOmens(Plugin plugin) {
        GlobalRegionScheduler globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
        // generate new dice
        Random r = new Random();
        // get methods for Harvest moon
        TotoroDance totoroDance = new TotoroDance();

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
                    // Reset defaults every dawn
                    totoroDance.setRandomTickSpeed(world, 3);
                    GlobalVars.harvestMoonToday = false;
                    GlobalVars.harvestMoonNow = false;
                    GlobalVars.bloodMoonToday = false;
                    GlobalVars.bloodMoonNow = false;
                    // get the moon phase tonight
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    // handle debugging flag
                    if (GlobalVars.debug) {
                        PlayerMessage.Send(playerList, "DEBUG Mode Enabled: Constant Blood Moon", NamedTextColor.WHITE);
                        GlobalVars.bloodMoonToday = true;
                    } else if (moonPhase == MoonPhase.FULL_MOON) {
                        // Do a dice roll to check if we're getting a harvest moon?
                        int chance = r.nextInt(2);
                        if (chance == 0) {
                            GlobalVars.harvestMoonToday = true;
                            PlayerMessage.Send(playerList, "Harvest moon tonight.", NamedTextColor.GOLD);
                        } else {
                            PlayerMessage.Send(playerList, "Full moon tonight.", NamedTextColor.YELLOW);
                        }
                    } else if (moonPhase == MoonPhase.NEW_MOON) {
                        // Do a dice roll to check if the players are THAT unlucky..
                        int chance = r.nextInt(2);
                        if (chance == 0) {
                            GlobalVars.bloodMoonToday = true;
                            PlayerMessage.Send(playerList, "Blood moon tonight.", NamedTextColor.DARK_RED);
                        } else {
                            PlayerMessage.Send(playerList, "New moon tonight.", NamedTextColor.DARK_GRAY);
                        }
                    }
                }
                // Execute immediately before the start of dusk
                if (time >= 12180 && time < 12200) {
                    if (GlobalVars.harvestMoonToday) {
                        GlobalVars.harvestMoonNow = true;
                        totoroDance.setRandomTickSpeed(world, 30);
                        totoroDance.setClearSkies(world, (24000 - (int)time));
                        PlayerMessage.Send(playerList, "You.. hear grass growing?", NamedTextColor.GOLD);
                    }
                }
                // Execute exactly at the start of night
                if (time >= 12980 && time < 13000) {
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    for (Player p : playerList) {
                        NightEffects.ApplyMoonlight(p, moonPhase, (24000 - (int)time));
                    }
                    if (GlobalVars.bloodMoonToday) {
                        GlobalVars.bloodMoonNow = true;
                    }
                }
            }
        }, 1L, 20L); // Check every 20 ticks (1 second)
    }
}
