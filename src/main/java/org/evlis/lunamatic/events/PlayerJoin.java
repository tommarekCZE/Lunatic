package org.evlis.lunamatic.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;

import org.evlis.lunamatic.Lunamatic;
import org.evlis.lunamatic.triggers.NightEffects;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.jetbrains.annotations.NotNull;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        long time = world.getTime();
        if (moonPhase == MoonPhase.FULL_MOON) {
            PlayerMessage.Send(player, "Full moon tonight.", NamedTextColor.YELLOW);
            NightEffects.ApplyMoonlight(player, MoonPhase.FULL_MOON, (int)time);
        } else if (moonPhase == MoonPhase.NEW_MOON) {
            if (Lunamatic.bloodMoonToday) {
                PlayerMessage.Send(player, "Blood moon tonight.", NamedTextColor.DARK_RED);
            } else {
                PlayerMessage.Send(player, "New moon tonight.", NamedTextColor.DARK_GRAY);
            }
            NightEffects.ApplyMoonlight(player, MoonPhase.NEW_MOON, (24000 - (int)time));
        }
    }
}
