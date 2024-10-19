package org.evlis.lunamatic.events;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimeSkip implements Listener {
    @EventHandler
    public void onSleepyTimeSkip(TimeSkipEvent event) {
        World world = event.getWorld();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();

        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP && (moonPhase ==  MoonPhase.FULL_MOON || moonPhase == MoonPhase.NEW_MOON)) {
            List<Player> players = world.getPlayers();
            for (Player p : players) {
                p.clearActivePotionEffects();
            }
        }
    }
}
