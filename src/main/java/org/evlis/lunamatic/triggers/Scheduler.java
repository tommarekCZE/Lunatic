package org.evlis.lunamatic.triggers;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.evlis.lunamatic.utilities.ResetFlags;
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
                    if (GlobalVars.debug) {
                        plugin.getComponentLogger().debug(Component.text("Resetting defaults for the day..."));
                    }
                    // Reset defaults every dawn
                    ResetFlags.resetAll();
                    ResetFlags.resetTickSpeed(world);

                    // get the moon phase tonight
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    // handle debugging flag
                    if (moonPhase == MoonPhase.FULL_MOON) {
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
                // Execute immediately after sunset starts
                if (time >= 12010 && time < 12030) {
                    if (GlobalVars.harvestMoonToday && !GlobalVars.harvestMoonNow) {
                        GlobalVars.harvestMoonNow = true;
                        // Ensure global var reset
                        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                            ResetFlags.resetTickSpeed(world);
                        }, 24000 - (int)time);
                        plugin.getServer().getScheduler().runTaskLater(plugin, ResetFlags::resetAll, 24000 - (int)time);
                        totoroDance.setRandomTickSpeed(world, 30);
                        totoroDance.setClearSkies(world, (24000 - (int)time));
                        PlayerMessage.Send(playerList, "You.. hear grass growing?", NamedTextColor.GOLD);
                    } else { // if for some reason both flags are still true, we have an invalid state
                        plugin.getComponentLogger().debug(Component.text("Invalid harvest moon detected!"));
                        GlobalVars.harvestMoonToday = false;
                        GlobalVars.harvestMoonNow = false;
                    }
                }
                // Execute exactly at the start of night
                if (time >= 12980 && time < 13000) {
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    for (Player p : playerList) {
                        NightEffects.ApplyMoonlight(p, moonPhase, (24000 - (int)time));
                    }
                    if (GlobalVars.bloodMoonToday && !GlobalVars.bloodMoonNow) {
                        GlobalVars.bloodMoonNow = true;
                        // Ensure global var reset
                        plugin.getServer().getScheduler().runTaskLater(plugin, ResetFlags::resetAll, 24000 - (int)time);
                    } else { // if for some reason both flags are still true, we have an invalid state
                        plugin.getComponentLogger().debug(Component.text("Invalid blood moon detected!"));
                        GlobalVars.bloodMoonToday = false;
                        GlobalVars.bloodMoonNow = false;
                    }
                }
            }
        }, 1L, 20L); // Check every 20 ticks (1 second)
    }
}
